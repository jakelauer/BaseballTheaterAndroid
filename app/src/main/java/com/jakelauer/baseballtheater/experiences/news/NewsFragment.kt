package com.jakelauer.baseballtheater.experiences.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.animation.TranslateAnimation
import com.jakelauer.baseballtheater.BuildConfig
import com.jakelauer.baseballtheater.MainActivity
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.common.listitems.EmptyListIndicator
import com.jakelauer.baseballtheater.experiences.settings.SettingsActivity
import com.jakelauer.baseballtheater.utils.PrefUtils
import libs.ButterKnife.bindView
import libs.RssParser.Article
import libs.RssParser.Parser
import org.jsoup.helper.StringUtil


/**
 * Created by Jake on 10/27/2017.
 */

class NewsFragment : RefreshableListFragment<NewsFragment.Model>()
{
	private lateinit var m_feedUrl: String
	private var m_url: String = ""
	private var m_allarticles = ArrayList<Article>()
	private var m_displayedarticles = ArrayList<Article>()
	private var m_isBeta: Boolean = false

	var m_clearSeen: FloatingActionButton by bindView(R.id.NEWS_clear_seen)
	var m_keepReadItems: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		val context = context

		if (context != null)
		{
			m_isBeta = BuildConfig.BETA

			val favTeam = PrefUtils.getString(context, PrefUtils.BEHAVIOR_FAVORITE_TEAM)
			m_feedUrl = if (m_isBeta) "https://beta.baseball.theater/data/news?favTeam=$favTeam&feeds=" else "https://baseball.theater/data/news?favTeam=$favTeam&feeds="

			setHasOptionsMenu(true)

			m_keepReadItems = PrefUtils.getBoolean(context, PrefUtils.ARTICLES_KEEP_READ)

			(activity as MainActivity).resetTitle()
		}
		else
		{
			throw Exception("Context is null")
		}
	}

	override fun onBindView()
	{
		super.onBindView()

		val itemTouchHelper = ItemTouchHelper(SwipeListener())
		itemTouchHelper.attachToRecyclerView(m_parentList)

		m_clearSeen.setOnClickListener({
			m_adapter.let {
				val items = it?.m_items
				if (items != null)
				{
					var removed = 0
					for (i in 0 until items.size)
					{
						val fixedI = i - removed
						val item = items[fixedI]
						val data = item.m_data as ArticleItem.Data
						if (data.m_seen)
						{
							val itemView = m_parentList.layoutManager.findViewByPosition(fixedI)

							if (itemView != null)
							{
								val animation = TranslateAnimation(0f, 500f, 0f, 0f) //May need to check the direction you want.
								animation.duration = 1000
								animation.fillAfter = true
								itemView.startAnimation(animation)
							}

							it.removeAt(fixedI)
							m_displayedarticles.removeAt(fixedI)
							removed++
						}
					}
				}
			}
		})
	}

	override fun getLayoutResourceId() = R.layout.news_fragment

	override fun createModel() = Model()

	override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater)
	{
		menuInflater.inflate(R.menu.news_options, menu)
		super.onCreateOptionsMenu(menu, menuInflater)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean
	{
		var handled = super.onOptionsItemSelected(item)

		when (item?.itemId)
		{
			R.id.game_list_settings ->
			{
				val intent = Intent(context, SettingsActivity::class.java)
				context?.startActivity(intent)
				handled = true
			}
		}
		return handled
	}

	override fun loadData()
	{
		val prefs = PreferenceManager.getDefaultSharedPreferences(context)
		val teamSources = prefs.getStringSet("team_news_sources", HashSet<String>())
		val sources = prefs.getStringSet("news_sources", HashSet<String>())

		val allSources = ArrayList<String>()
		allSources += teamSources
		allSources += sources

		val cslFeeds = StringUtil.join(allSources, ",") ?: ""
		m_url = m_feedUrl + cslFeeds

		m_refreshView.isRefreshing = true

		val parser = Parser()
		parser.execute(m_url)
		parser.onFinish(object : Parser.OnTaskCompleted
		{
			override fun onTaskCompleted(list: ArrayList<Article>)
			{
				finalizeArticles(list)
			}

			override fun onError()
			{
				finalizeArticles(null)
			}
		})
	}

	private fun finalizeArticles(articleList: List<Article>?)
	{
		activity?.runOnUiThread({
			m_allarticles.clear()
			m_displayedarticles.clear()
			if (articleList != null)
			{
				m_allarticles.addAll(articleList)
			}

			onFinished()
		})
	}

	fun markAsSeen(position: Int)
	{
		context?.let {
			val cleared = m_displayedarticles[position]

			val link = cleared.link
			if (link != null)
			{
				val seen = ArrayList<String>(PrefUtils.getStringSet(it, PrefUtils.ARTICLES_SEEN))
				seen.add(link)
				PrefUtils.set(it, PrefUtils.ARTICLES_SEEN, seen.toSet())

				m_adapter?.notifyItemChanged(position)
				m_adapter?.notifyDataSetChanged()
			}
		}
	}

	private fun onFinished()
	{
		m_refreshView.isRefreshing = false
		m_adapter?.clear()

		context?.let {
			val seenItems = PrefUtils.getStringSet(it, PrefUtils.ARTICLES_SEEN)

			if (m_allarticles.size > 0)
			{
				for (article in m_allarticles)
				{
					if (article.link == "")
					{
						continue
					}

					val seen = seenItems.contains(article.link)

					if (!seen || m_keepReadItems)
					{
						val articleItem = ArticleItem(ArticleItem.Data(article, seen))

						articleItem.setClickListener({ _, position ->
							articleItem.setSeen()
							markAsSeen(position)

							val builder = CustomTabsIntent.Builder()
							val customTabsIntent = builder.build()
							customTabsIntent.launchUrl(it, Uri.parse(articleItem.m_data.m_article.link))
						})

						m_displayedarticles.add(article)

						m_adapter?.add(articleItem)
					}
				}
			}
			else
			{
				val emptyItem = EmptyListIndicator(EmptyListIndicator.Model(it, R.string.article_list_empty, R.drawable.ic_cloud_off_black_24px))
				m_adapter?.add(emptyItem)
			}
		}
	}

	fun removeArticle(position: Int)
	{
		m_adapter?.removeAt(position)
		m_displayedarticles.removeAt(position)
	}

	class Model

	inner class SwipeListener : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
	{
		override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean
		{
			return false
		}

		override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int)
		{
			if (viewHolder != null)
			{
				val position = viewHolder.adapterPosition

				markAsSeen(position)
				removeArticle(position)
			}
		}

	}
}
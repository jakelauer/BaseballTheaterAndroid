package com.jakelauer.baseballtheater.experiences.news

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.jakelauer.baseballtheater.BuildConfig
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.common.listitems.EmptyListIndicator
import com.jakelauer.baseballtheater.experiences.settings.SettingsActivity
import com.jakelauer.baseballtheater.utils.PrefUtils
import libs.RssParser.Article
import libs.RssParser.Parser
import org.jsoup.helper.StringUtil


/**
 * Created by Jake on 10/27/2017.
 */

class NewsFragment : RefreshableListFragment<NewsFragment.Model>
{
	private lateinit var m_feedUrl: String
	private var m_url: String = ""
	private var m_allarticles = ArrayList<Article>()
	private var m_displayedarticles = ArrayList<Article>()
	private var m_isBeta: Boolean = false

	constructor() : super()

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		m_isBeta = BuildConfig.BETA

		val favTeam = PrefUtils.getString(context, PrefUtils.BEHAVIOR_FAVORITE_TEAM)
		m_feedUrl = if (m_isBeta) "https://dev.baseball.theater/data/news?favTeam=$favTeam&feeds=" else "https://baseball.theater/data/news?feeds="

		setHasOptionsMenu(true)
	}

	override fun onBindView()
	{
		super.onBindView()

		val itemTouchHelper = ItemTouchHelper(SwipeListener())
		itemTouchHelper.attachToRecyclerView(m_parentList)
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
				context.startActivity(intent)
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
		sources.addAll(teamSources)

		val cslFeeds = StringUtil.join(sources, ",") ?: ""
		m_url = m_feedUrl + cslFeeds

		m_refreshView.isRefreshing = true

		val parser = Parser(m_isBeta)
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
		activity.runOnUiThread({
			m_allarticles.clear()
			m_displayedarticles.clear()
			if (articleList != null)
			{
				m_allarticles.addAll(articleList)
			}

			onFinished()
		})
	}

	private fun onFinished()
	{
		m_refreshView.isRefreshing = false
		m_adapter?.clear()

		val seen = PrefUtils.getStringSet(context, PrefUtils.ARTICLES_SEEN)

		if (m_allarticles.size > 0)
		{
			for (article in m_allarticles)
			{
				if(!seen.contains(article.link))
				{
					val articleItem = ArticleItem(article)

					m_displayedarticles.add(article)

					m_adapter?.add(articleItem)
				}
			}
		}
		else
		{
			val emptyItem = EmptyListIndicator(EmptyListIndicator.Model(context, R.string.article_list_empty, R.drawable.ic_cloud_off_black_24px))
			m_adapter?.add(emptyItem)
		}
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
				val cleared = m_displayedarticles[viewHolder.adapterPosition]

				val link = cleared.link
				if(link != null)
				{
					val seen = ArrayList<String>(PrefUtils.getStringSet(context, PrefUtils.ARTICLES_SEEN))
					seen.add(link)

					PrefUtils.set(context, PrefUtils.ARTICLES_SEEN, seen.toSet())
				}

				m_displayedarticles.removeAt(viewHolder.adapterPosition)
				m_adapter?.notifyItemRemoved(viewHolder.adapterPosition)
			}
		}

	}
}
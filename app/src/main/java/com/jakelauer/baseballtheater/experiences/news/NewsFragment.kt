package com.jakelauer.baseballtheater.experiences.news

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.jakelauer.baseballtheater.BuildConfig
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.common.listitems.EmptyListIndicator
import com.jakelauer.baseballtheater.experiences.settings.SettingsActivity
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
	private var m_articles = ArrayList<Article>()
	private var m_isBeta: Boolean = false

	constructor() : super()

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		m_isBeta = BuildConfig.BETA

		m_feedUrl = if (m_isBeta) "https://dev.baseball.theater/data/news?feeds=" else "https://baseball.theater/data/news?feeds="

		setHasOptionsMenu(true)
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
		val sources = prefs.getStringSet("news_sources", HashSet<String>())

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
			if (articleList != null)
			{
				m_articles.clear()
				m_articles.addAll(articleList)
			}

			onFinished()
		})
	}

	private fun onFinished()
	{
		m_refreshView.isRefreshing = false
		m_adapter?.clear()

		if (m_articles.size > 0)
		{
			for (article in m_articles)
			{
				val articleItem = ArticleItem(article)

				m_adapter?.add(articleItem)
			}
		}
		else
		{
			val emptyItem = EmptyListIndicator(EmptyListIndicator.Model(context, R.string.article_list_empty, R.drawable.ic_cloud_off_black_24px))
			m_adapter?.add(emptyItem)
		}
	}

	class Model

	inner class ParserTask : Parser.OnTaskCompleted
	{
		override fun onTaskCompleted(list: ArrayList<Article>)
		{
		}

		override fun onError()
		{
		}
	}
}
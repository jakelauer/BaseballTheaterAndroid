package com.jakelauer.baseballtheater.experiences.news

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.prof.rssparser.Article
import com.prof.rssparser.Parser


/**
 * Created by Jake on 10/27/2017.
 */

class NewsFragment : RefreshableListFragment<NewsFragment.Model>
{
	private var m_urls = ArrayList<String>()
	private var m_sitesLoaded = 0
	private var m_articles = ArrayList<Article>()

	constructor() : super()

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		val prefs = PreferenceManager.getDefaultSharedPreferences(context)
		val sources = prefs.getStringSet("news_sources", HashSet<String>())

		for (source in sources)
		{
			val url = getUrlFromSource(source)
			if (url != null)
			{
				m_urls.add(url)
			}
		}
	}

	override fun getLayoutResourceId() = R.layout.news_fragment

	override fun createModel() = Model()

	override fun loadData()
	{
		m_refreshView.isRefreshing = true

		var urlsToLoad = m_urls.size
		var articleLists = HashMap<String, ArrayList<Article>>()
		for (url in m_urls)
		{
			val parser = Parser()
			parser.execute(url)
			parser.onFinish(object : Parser.OnTaskCompleted
			{
				override fun onTaskCompleted(list: ArrayList<Article>)
				{
					articleLists.put(url, list)
					m_sitesLoaded += 1

					if (m_sitesLoaded == urlsToLoad)
					{
						finalizeArticles(articleLists)
					}
				}

				override fun onError()
				{
					urlsToLoad--

					if (m_sitesLoaded == urlsToLoad)
					{
						finalizeArticles(articleLists)
					}
				}
			})
		}
	}

	private fun finalizeArticles(articleLists: HashMap<String, ArrayList<Article>>)
	{
		val articleListList = articleLists.values
		val allArticles = ArrayList<Article>()

		articleListList.flatMapTo(allArticles) { it }

		allArticles.sortByDescending { it.pubDate }

		m_articles = allArticles

		onFinished()
	}

	private fun onFinished()
	{
		m_refreshView.isRefreshing = false
		m_adapter?.clear()

		for (article in m_articles)
		{
			val articleItem = ArticleItem(article, activity)

			articleItem

			m_adapter?.add(articleItem)
		}
	}

	private fun getUrlFromSource(source: String?) = when (source)
	{
		"mlb" -> "http://mlb.mlb.com/partnerxml/gen/news/rss/mlb.xml"
		"fangraphs" -> "http://www.fangraphs.com/blogs/feed/"
		"si" -> "https://www.si.com/rss/si_mlb.rss"
		"538" -> "https://fivethirtyeight.com/tag/mlb/feed/"
		"espn" -> "http://www.espn.com/espn/rss/mlb/news"
		else -> null
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
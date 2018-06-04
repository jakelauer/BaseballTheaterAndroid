package com.jakelauer.baseballtheater.experiences.search

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.View
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightSearchResult
import com.jakelauer.baseballtheater.MlbDataServer.Search
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.highlights.HighlightItem
import com.mancj.materialsearchbar.MaterialSearchBar
import libs.ButterKnife.bindView


/**
 * Created by Jake on 2/18/2018.
 */
class SearchFragment : RefreshableListFragment<Any>()
{
	var m_query: String? = null
	val m_searchBar: MaterialSearchBar by bindView(R.id.SEARCH_query)

	val PER_PAGE: Int = 20
	var m_nextPage: Int = 0

	var m_results: ArrayList<HighlightSearchResult> = ArrayList()

	override fun getLayoutResourceId() = R.layout.search_fragment

	override fun onBindView()
	{
		super.onBindView()

		setEmpty(false)
		m_searchBar.enableSearch()
		m_searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener
		{
			override fun onButtonClicked(buttonCode: Int)
			{
				setEmpty(false)
			}

			override fun onSearchStateChanged(enabled: Boolean)
			{
				setEmpty(false)
			}

			override fun onSearchConfirmed(query: CharSequence)
			{
				if (query != m_query)
				{
					setEmpty(false)
				}

				m_query = query.toString()
				m_searchBar.requestFocus()
				loadData()
			}
		})
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		activity?.title = "Search"
	}

	override fun createModel(): Any
	{
		return ""
	}

	override fun loadData()
	{
		m_query?.let { query ->
			m_refreshView.isRefreshing = true
			val s = Search()
			s.searchHighlights(DownloadListener { result -> onLoaded(result) }, query, PER_PAGE, m_nextPage)
		}

		m_nextPage++
	}

	fun reset()
	{
		m_results = ArrayList()
		m_nextPage = 0
	}

	fun setEmpty(searchTermSpecified: Boolean)
	{
		m_adapter?.clear()
		reset()
		m_adapter?.add(SearchEmptyItem(searchTermSpecified))
	}

	fun onLoaded(result: List<HighlightSearchResult>?)
	{
		m_refreshView.isRefreshing = false

		if (result != null && result.any())
		{
			m_adapter?.clear()

			val oldResults = m_results
			m_results = ArrayList(oldResults)
			m_results.addAll(result)

			for (highlightResult in m_results)
			{
				val highlight = highlightResult.highlight
				if (highlight != null)
				{
					val highlightItem = HighlightItem(HighlightItem.HighlightData(true, highlight, highlightResult.thumbnails), activity as BaseActivity)

					m_adapter?.add(highlightItem)
				}
			}

			val resultCount = m_results.count()
			if (resultCount % PER_PAGE == 0 && resultCount > 0)
			{
				m_adapter?.add(LoadMoreItem(View.OnClickListener {
					loadData()
				}))
			}
		}
		else
		{
			setEmpty(true)
		}
	}
}
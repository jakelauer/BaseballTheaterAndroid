package com.jakelauer.baseballtheater.experiences.search

import android.support.v7.widget.SearchView
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

	override fun getLayoutResourceId() = R.layout.search_fragment

	override fun onBindView()
	{
		super.onBindView()

		m_searchBar.enableSearch()
		m_searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener
		{
			override fun onButtonClicked(buttonCode: Int)
			{
				m_adapter?.clear()
			}

			override fun onSearchStateChanged(enabled: Boolean)
			{
				m_adapter?.clear()
			}

			override fun onSearchConfirmed(query: CharSequence)
			{
				m_query = query.toString()
				loadData()
			}
		})
	}

	override fun createModel(): Any
	{
		return ""
	}

	override fun loadData()
	{
		m_query?.let {
			m_refreshView.isRefreshing = true
			val s = Search()
			s.searchHighlights(it, DownloadListener { result -> onLoaded(result) })
		}
	}

	fun onLoaded(result: List<HighlightSearchResult>?)
	{
		m_refreshView.isRefreshing = false
		m_adapter?.clear()

		if (result != null)
		{
			for (highlight in result)
			{
				val highlightItem = HighlightItem(HighlightItem.HighlightData(highlight), activity as BaseActivity)

				m_adapter?.add(highlightItem)
			}
		}
	}
}
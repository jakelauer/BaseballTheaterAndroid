package com.jakelauer.baseballtheater.experiences.search

import android.support.v7.widget.SearchView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightSearchResult
import com.jakelauer.baseballtheater.MlbDataServer.Search
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.highlights.HighlightItem
import libs.ButterKnife.bindView


/**
 * Created by Jake on 2/18/2018.
 */
class SearchFragment : RefreshableListFragment<Any>()
{
	var m_query: String? = null
	val m_searchBar: SearchView by bindView(R.id.SEARCH_query)

	override fun getLayoutResourceId() = R.layout.search_fragment

	override fun onBindView()
	{
		super.onBindView()

		m_searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener
		{
			override fun onQueryTextSubmit(query: String?): Boolean
			{
				m_query = query
				loadData()
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean
			{
				return false
			}
		})

		val context = context ?: throw Exception("Context cannot be null")
	}

	override fun createModel(): Any
	{
		return ""
	}

	override fun loadData()
	{
		m_query?.let {
			val s = Search()
			s.searchHighlights(it, DownloadListener { result -> onLoaded(result) })
		}
	}

	fun onLoaded(result: List<HighlightSearchResult>?)
	{
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
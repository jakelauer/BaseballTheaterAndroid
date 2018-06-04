package com.jakelauer.baseballtheater.MlbDataServer

import com.jakelauer.baseballtheater.BuildConfig
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightSearchResult
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener
import com.jakelauer.baseballtheater.MlbDataServer.Utils.JsonLoader


/**
 * Created by Jake on 1/14/2017.
 */

class Search
{
	@Suppress("UNCHECKED_CAST")
	fun searchHighlights(downloadListener: DownloadListener<List<HighlightSearchResult>>, query: String, perPage: Int = 20, page: Int = 0)
	{
		val jsonLoader = JsonLoader()
		val stringDlListener = DownloadListener<String> { result ->
			val resultMapped = SearchMapper.map(result)
			downloadListener.onDownloadComplete(resultMapped.toList())
		}
		jsonLoader.GetJson(getUrl(query, perPage, page), stringDlListener)
	}

	private fun getUrl(query: String, perPage: Int, page: Int): String
	{
		return "$url?query=$query&page=$page&perpage=$perPage"
	}

	companion object
	{
		private const val url = "https://search.baseball.theater/api/Search/Highlights"
	}
}

typealias HighlightSearchResultList = List<HighlightSearchResult>
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
	fun searchHighlights(query: String, downloadListener: DownloadListener<List<HighlightSearchResult>>)
	{
		val jsonLoader = JsonLoader()
		val stringDlListener = DownloadListener<String> { result ->
			try
			{
				val resultMapped = SearchMapper.map(result)
				downloadListener.onDownloadComplete(resultMapped.toList())
			}
			catch(e: Exception){

			}
		}
		jsonLoader.GetJson(getUrl(query), stringDlListener)
	}

	private fun getUrl(query: String): String
	{
		return "$url?query=$query&page=0&perpage=20"
	}

	companion object
	{
		private val url = if (BuildConfig.BETA) "https://beta.baseball.theater/Data/SearchHighlights" else "https://baseball.theater/Data/SearchHighlights"
	}
}

typealias HighlightSearchResultList = List<HighlightSearchResult>
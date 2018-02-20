package com.jakelauer.baseballtheater.MlbDataServer

import com.google.gson.reflect.TypeToken
import com.jakelauer.baseballtheater.BuildConfig
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightSearchResult
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener
import com.jakelauer.baseballtheater.MlbDataServer.Utils.JsonLoader
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


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
			val mapper = jacksonObjectMapper()
			val resultMapped: List<HighlightSearchResult> = mapper.readValue(result)
			downloadListener.onDownloadComplete(resultMapped)
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
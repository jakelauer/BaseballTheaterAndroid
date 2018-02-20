package com.jakelauer.baseballtheater.MlbDataServer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.*
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener
import com.jakelauer.baseballtheater.MlbDataServer.Utils.JsonLoader
import com.jakelauer.baseballtheater.MlbDataServer.Utils.XmlLoader

import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by Jake on 1/14/2017.
 */

class PatreonDataCreator
{

	operator fun get(downloadListener: DownloadListener<JSONObject>)
	{
		val jsonLoader = JsonLoader()
		val stringDlListener = DownloadListener<String> { result ->
			val mapper = jacksonObjectMapper()
			val resultMapped: JSONObject = mapper.readValue(result)
			downloadListener.onDownloadComplete(resultMapped)
		}
		jsonLoader.GetJson(url, stringDlListener)
	}

	companion object
	{
		private val url = "https://baseball.theater/Data/Patreon"
	}
}
package com.jakelauer.baseballtheater.MlbDataServer.Utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

import org.json.JSONObject
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

import java.lang.reflect.Type

class JsonLoader
{
	fun GetJson(url: String, downloadListener: DownloadListener<String>)
	{
		val jsonDownloader = JsonDownloader(DownloadListener<String> { response ->
			if (response != null && !response.isEmpty())
			{
				try
				{
					downloadListener.onDownloadComplete(response)
				}
				catch (e: Exception)
				{
					e.printStackTrace()
				}

			}
			else
			{
				downloadListener.onDownloadComplete(null)
			}
		})

		jsonDownloader.execute(url)
	}
}


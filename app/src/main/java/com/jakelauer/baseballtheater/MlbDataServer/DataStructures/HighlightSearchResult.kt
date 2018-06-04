package com.jakelauer.baseballtheater.MlbDataServer.DataStructures

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime
import org.simpleframework.xml.Attribute
import java.util.regex.Pattern

/**
 * Created by Jake on 2/18/2018.
 */

class HighlightSearchResult
{
	@JsonProperty("gameId")
	var gameId: Int? = null

	@JsonProperty("highlight")
	var highlight: Highlight? = null

	@JsonProperty("thumbnails")
	var thumbnails: HighlightThumbs? = null

/*	fun getDurationMilliseconds(): Long
	{
		val pieces = duration?.split(":".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()
		if (pieces != null)
		{
			val h = Integer.valueOf(pieces[0])
			val m = Integer.valueOf(pieces[1])
			val s = Integer.valueOf(pieces[2])

			return s * 1000L + (m * 60 * 1000).toLong() + (h * 60 * 60 * 1000).toLong()
		}
		return 0
	}

	private fun parseDateTimeMsft(dateString: String?): DateTime?
	{
		var result: DateTime? = null

		if(dateString != null)
		{
			val datePattern = Pattern.compile("^/Date\\((\\d+)([+-]\\d+)?\\)/$")
			val m = datePattern.matcher(dateString)
			if (m.matches())
			{
				val l = java.lang.Long.parseLong(m.group(1))
				result = DateTime(l)
			}
		}

		return result
	}*/
}

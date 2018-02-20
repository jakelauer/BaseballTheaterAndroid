package com.jakelauer.baseballtheater.MlbDataServer.DataStructures

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.joda.time.DateTime

/**
 * Created by Jake on 2/18/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
class HighlightSearchResult
{
	var Id: Int? = null
	var Date: DateTime? = null
	var Recap: Boolean? = null
	var Condensed: Boolean? = null
	var TeamId: Int? = null
	var TeamName: String? = null
	var PlayerIds: String? = null
	var PlayerNames: String? = null
	var Headline: String? = null
	var Duration: String? = null
	var Blurb: String? = null
	var BigBlurb: String? = null
	var Thumb_s: String? = null
	var Thumb_m: String? = null
	var Thumb_l: String? = null
	var Video_s: String? = null
	var Video_m: String? = null
	var Video_l: String? = null
	var GameId: Int? = null

	fun getDurationMilliseconds(): Long
	{
		val pieces = Duration?.split(":".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()
		if (pieces != null)
		{
			val h = Integer.valueOf(pieces[0])
			val m = Integer.valueOf(pieces[1])
			val s = Integer.valueOf(pieces[2])

			return s * 1000L + (m * 60 * 1000).toLong() + (h * 60 * 60 * 1000).toLong()
		}
		return 0
	}
}

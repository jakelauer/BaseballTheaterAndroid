package com.jakelauer.baseballtheater.MlbDataServer.DataStructures

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime
import java.util.regex.Pattern

/**
 * Created by Jake on 2/18/2018.
 */

class HighlightSearchResult
{
	@JsonProperty("Id")
	var id: Int? = null

	@JsonProperty("Date")
	var dateTimeString: String? = null

	val date: DateTime?
		get() = if (dateTimeString != null)
		{
			parseDateTimeMsft(dateTimeString)
		}
		else null

	@JsonProperty("Recap")
	var recap: Boolean? = null

	@JsonProperty("Condensed")
	var condensed: Boolean? = null

	@JsonProperty("TeamId")
	var teamId: Int? = null

	@JsonProperty("TeamName")
	var teamName: String? = null

	@JsonProperty("PlayerIds")
	var playerIds: String? = null

	@JsonProperty("PlayerNames")
	var playerNames: String? = null

	@JsonProperty("Headline")
	var headline: String? = null

	@JsonProperty("Duration")
	var duration: String? = null

	@JsonProperty("Blurb")
	var blurb: String? = null

	@JsonProperty("BigBlurb")
	var bigBlurb: String? = null

	@JsonProperty("Thumb_s")
	var thumb_s: String? = null

	@JsonProperty("Thumb_m")
	var thumb_m: String? = null

	@JsonProperty("Thumb_l")
	var thumb_l: String? = null

	@JsonProperty("Video_s")
	var video_s: String? = null

	@JsonProperty("Video_m")
	var video_m: String? = null

	@JsonProperty("Video_l")
	var video_l: String? = null

	@JsonProperty("GameId")
	var gameId: Int? = null

	fun getDurationMilliseconds(): Long
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
	}
}

package com.jakelauer.baseballtheater.utils

import org.joda.time.DateTime

/**
 * Created by Jake on 11/7/2017.
 */

class DateTimeUtils
{
	companion object
	{
		fun getDefaultDate(): DateTime
		{
			val newYearsDay2018 = DateTime("2018-01-01")
			val finalDay2017 = DateTime("2017-11-01")
			val springTraining2018 = DateTime("2018-02-23")
			val now = DateTime.now()

			return when
			{
				now.isBefore(newYearsDay2018) -> finalDay2017
				now.isAfter(newYearsDay2018) && now.isBefore(springTraining2018) -> springTraining2018
				else -> now
			}
		}

		fun getIsOffseason(): Boolean
		{
			val springTraining2018 = DateTime("2018-02-23")
			val now = DateTime.now()

			return now.isBefore(springTraining2018)
		}
	}
}
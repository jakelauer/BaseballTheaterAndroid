package com.jakelauer.baseballtheater.experiences.gamelist

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.ParseException


/**
 * Created by Jake on 10/29/2017.
 */

internal class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener
{
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
	{
		val parent = parentFragment as GameListPagerFragment
		val currentDate = parent.getCurrentDate()

		// Use the current date as the default date in the picker
		val year = currentDate.year
		val month = currentDate.monthOfYear - 1
		val day = currentDate.dayOfMonth

		// Create a new instance of DatePickerDialog and return it
		return DatePickerDialog(activity, this, year, month, day)
	}

	override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int)
	{

		val dateString = year.toString() + "/" + (month + 1) + "/" + day
		val fmt = DateTimeFormat.forPattern("yyyy/MM/dd")

		try
		{
			val parent = parentFragment as GameListPagerFragment
			parent.refreshWithDate(DateTime.parse(dateString, fmt))
		}
		catch (e: ParseException)
		{
			e.printStackTrace()
		}

	}
}
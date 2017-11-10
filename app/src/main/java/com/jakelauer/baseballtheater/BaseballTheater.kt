package com.jakelauer.baseballtheater

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.facebook.stetho.Stetho

/**
 * Created by Jake on 1/21/2017.
 */

class BaseballTheater : Application()
{
	lateinit var m_prefs: SharedPreferences
	private set

	override fun onCreate()
	{
		super.onCreate()
		Stetho.initializeWithDefaults(this)

		m_prefs = PreferenceManager.getDefaultSharedPreferences(this)
	}
}
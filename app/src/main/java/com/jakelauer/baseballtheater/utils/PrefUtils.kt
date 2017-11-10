package com.jakelauer.baseballtheater.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.util.ArraySet
import com.jakelauer.baseballtheater.BaseballTheater

/**
 * Created by Jake on 10/29/2017.
 */

class PrefUtils
{
	companion object
	{
		val BEHAVIOR_FAVORITE_TEAM = "behavior_favorite_team"
		val BEHAVIOR_HIDE_SCORES = "behavior_hide_scores"
		val NEWS_SHOW_IN_OFFSEASON = "news_show_in_offseason"
		val SEEN_NUX = "seen_nux";
		val ARTICLES_SEEN = "articles_seen"

		private fun getApplication(context: Context): BaseballTheater
		{
			return (context.applicationContext as BaseballTheater)
		}

		fun getString(context: Context, key: String): String
		{
			return getApplication(context).m_prefs.getString(key, "")
		}

		fun getBoolean(context: Context, key: String): Boolean
		{
			return getApplication(context).m_prefs.getBoolean(key, false)
		}

		fun getStringSet(context: Context, key: String): Set<String>
		{
			return getApplication(context).m_prefs.getStringSet(key, ArraySet<String>())
		}

		fun set(context: Context, key: String, value: Any)
		{
			val editor = getApplication(context).m_prefs.edit()
			editor.put(key, value)
			editor.apply()
		}

		@Suppress("UNCHECKED_CAST")
		private fun SharedPreferences.Editor.put(key: String, value: Any)
		{
			when (value)
			{
				is String -> putString(key, value)
				is Boolean -> putBoolean(key, value)
				is Float -> putFloat(key, value)
				is Int -> putInt(key, value)
				is Long -> putLong(key, value)
				is Set<*> -> putStringSet(key, value as Set<String>)
				is Array<*> -> putStringSet(key, value as Set<String>)
			}
		}
	}
}
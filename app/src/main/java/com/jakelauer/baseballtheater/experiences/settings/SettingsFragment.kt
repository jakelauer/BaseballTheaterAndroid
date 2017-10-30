package com.jakelauer.baseballtheater.experiences.settings

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.jakelauer.baseballtheater.R

/**
 * Created by Jake on 10/28/2017.
 */


class SettingsFragment : PreferenceFragment()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		addPreferencesFromResource(R.xml.settings)
		setHasOptionsMenu(true)

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences
		// to their values. When their values change, their summaries are
		// updated to reflect the new value, per the Android Design
		// guidelines.
		SettingsActivity.bindPreferenceSummaryToValue(findPreference("display_video_quality_mobile"))
		SettingsActivity.bindPreferenceSummaryToValue(findPreference("display_thumbnail_quality_mobile"))
		SettingsActivity.bindPreferenceSummaryToValue(findPreference("display_video_quality_wifi"))
		SettingsActivity.bindPreferenceSummaryToValue(findPreference("display_thumbnail_quality_wifi"))

		SettingsActivity.bindPreferenceSummaryToValue(findPreference("behavior_favorite_team"))
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		val id = item.itemId
		if (id == android.R.id.home)
		{
			startActivity(Intent(activity, SettingsActivity::class.java))
			return true
		}
		return super.onOptionsItemSelected(item)
	}
}
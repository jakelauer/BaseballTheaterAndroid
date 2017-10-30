package com.jakelauer.baseballtheater.experiences.settings


import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.view.MenuItem

import libs.AppCompatPreferenceActivity

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 *
 * See [
 * Android Design: Settings](http://developer.android.com/design/patterns/settings.html) for design guidelines and the [Settings
 * API Guide](http://developer.android.com/guide/topics/ui/settings.html) for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setupActionBar()

		fragmentManager.beginTransaction()
				.replace(android.R.id.content, SettingsFragment()).commit()
	}

	private fun setupActionBar()
	{
		val actionBar = supportActionBar
		if (actionBar != null)
		{
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true)
			actionBar.setHomeButtonEnabled(true)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			android.R.id.home ->
			{
				this.finish()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun isValidFragment(fragmentName: String): Boolean
	{
		return (PreferenceFragment::class.java.name == fragmentName
				|| SettingsFragment::class.java.name == fragmentName)
	}

	companion object
	{
		private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
			val stringValue = value.toString()

			if (preference is ListPreference)
			{
				val index = preference.findIndexOfValue(stringValue)

				preference.setSummary(if (index >= 0) preference.entries[index] else null)
			}
			else
			{
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.summary = stringValue
			}
			true
		}

		fun bindPreferenceSummaryToValue(preference: Preference)
		{
			// Set the listener to watch for value changes.
			preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

			// Trigger the listener immediately with the preference's
			// current value.
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
					PreferenceManager
							.getDefaultSharedPreferences(preference.context)
							.getString(preference.key, ""))
		}
	}
}
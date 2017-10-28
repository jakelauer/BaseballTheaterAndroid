package com.jakelauer.baseballtheater.experiences.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.MenuItem
import com.jakelauer.baseballtheater.R
import java.util.List


/**
 * Created by Jake on 10/27/2017.
 */


class SettingsActivity : PreferenceActivity()
{
	override fun onCreate(savedInstanceState: Bundle)
	{
		super.onCreate(savedInstanceState)
		setupActionBar()
	}

	/**
	 * Set up the [android.app.ActionBar], if the API is available.
	 */
	private fun setupActionBar()
	{
		if (actionBar != null)
		{
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true)
			actionBar.setHomeButtonEnabled(true)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.getItemId())
		{
			android.R.id.home ->
			{
				this.finish()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	/**
	 * {@inheritDoc}
	 */
	override fun onIsMultiPane(): Boolean
	{
		return isXLargeTablet(this)
	}

	/**
	 * {@inheritDoc}
	 */
	override fun onBuildHeaders(target: MutableList<Header>)
	{
		loadHeadersFromResource(R.xml.pref_headers, target)
	}

	/**
	 * This method stops fragment injection in malicious applications.
	 * Make sure to deny any unknown fragments here.
	 */
	override fun isValidFragment(fragmentName: String): Boolean
	{
		return (PreferenceFragment::class.java.name == fragmentName
				|| DisplayPreferenceFragment::class.java.name == fragmentName
				|| BehaviorPreferenceFragment::class.java.name == fragmentName)
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	class DisplayPreferenceFragment : PreferenceFragment()
	{
		override fun onCreate(savedInstanceState: Bundle?)
		{
			super.onCreate(savedInstanceState)
			addPreferencesFromResource(R.xml.pref_display)
			setHasOptionsMenu(true)

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("display_video_quality_mobile"))
			bindPreferenceSummaryToValue(findPreference("display_thumbnail_quality_mobile"))
			bindPreferenceSummaryToValue(findPreference("display_video_quality_wifi"))
			bindPreferenceSummaryToValue(findPreference("display_thumbnail_quality_wifi"))
		}

		override fun onOptionsItemSelected(item: MenuItem): Boolean
		{
			val id = item.getItemId()
			if (id == android.R.id.home)
			{
				startActivity(Intent(activity, SettingsActivity::class.java))
				return true
			}
			return super.onOptionsItemSelected(item)
		}
	}

	/**
	 * This fragment shows notification preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	class BehaviorPreferenceFragment : PreferenceFragment()
	{
		override fun onCreate(savedInstanceState: Bundle?)
		{
			super.onCreate(savedInstanceState)
			addPreferencesFromResource(R.xml.pref_behavior)
			setHasOptionsMenu(true)

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("behavior_favorite_team"))
		}

		override fun onOptionsItemSelected(item: MenuItem): Boolean
		{
			val id = item.getItemId()
			if (id == android.R.id.home)
			{
				startActivity(Intent(activity, SettingsActivity::class.java))
				return true
			}
			return super.onOptionsItemSelected(item)
		}
	}

	companion object
	{
		/**
		 * A preference value change listener that updates the preference's summary
		 * to reflect its new value.
		 */
		private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
			val stringValue = value.toString()

			if (preference is ListPreference)
			{
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				val index = preference.findIndexOfValue(stringValue)

				// Set the summary to reflect the new value.
				preference.setSummary(
						if (index >= 0)
							preference.entries[index]
						else
							null)

			}
			else if (preference is RingtonePreference)
			{
				// For ringtone preferences, look up the correct display value
				// using RingtoneManager.
				if (TextUtils.isEmpty(stringValue))
				{
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary(R.string.pref_ringtone_silent)

				}
				else
				{
					val ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue))

					if (ringtone == null)
					{
						// Clear the summary if there was a lookup error.
						preference.setSummary(null)
					}
					else
					{
						// Set the summary to reflect the new ringtone display
						// name.
						val name = ringtone.getTitle(preference.getContext())
						preference.setSummary(name)
					}
				}

			}
			else
			{
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.summary = stringValue
			}
			true
		}

		/**
		 * Helper method to determine if the device has an extra-large screen. For
		 * example, 10" tablets are extra-large.
		 */
		private fun isXLargeTablet(context: Context): Boolean
		{
			return context.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
		}

		/**
		 * Binds a preference's summary to its value. More specifically, when the
		 * preference's value is changed, its summary (line of text below the
		 * preference title) is updated to reflect the value. The summary is also
		 * immediately updated upon calling this method. The exact display format is
		 * dependent on the type of preference.
		 *
		 * @see .sBindPreferenceSummaryToValueListener
		 */
		private fun bindPreferenceSummaryToValue(preference: Preference)
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
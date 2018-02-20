package com.jakelauer.baseballtheater.experiences.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.customtabs.CustomTabsIntent
import android.view.MenuItem
import android.view.View
import com.jakelauer.baseballtheater.MlbDataServer.PatreonDataCreator
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener
import com.jakelauer.baseballtheater.R
import kotlinx.android.synthetic.main.patreon_preference.*
import org.json.JSONException
import org.json.JSONObject


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
		SettingsActivity.bindPreferenceSummaryToValue(findPreference("display_quality_mobile"))
		SettingsActivity.bindPreferenceSummaryToValue(findPreference("display_quality_wifi"))

		SettingsActivity.bindPreferenceSummaryToValue(findPreference("behavior_favorite_team"))

		val patreonDataCreator = PatreonDataCreator()
		patreonDataCreator[DownloadListener { response ->
			var percentageString: Double? = 0.0
			try
			{
				percentageString = response.get("GoalPercentage") as Double
			}
			catch (e: JSONException)
			{
				e.printStackTrace()
			}

			val realPercentage = percentageString!! * 100
			patreon_progress.progress = realPercentage.toInt()
			patreon_progress_text.text = "${realPercentage.toInt()}% of goal"
		}]
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		val patPref = findPreference("patreon")

		if (view != null)
		{
			patPref.setOnPreferenceClickListener { _ ->
				val url = "https://www.patreon.com/jakelauer"

				val builder = CustomTabsIntent.Builder()
				val customTabsIntent = builder.build()
				customTabsIntent.launchUrl(view.context, Uri.parse(url))

				false
			}
		}
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
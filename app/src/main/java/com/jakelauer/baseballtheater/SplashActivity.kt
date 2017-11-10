package com.jakelauer.baseballtheater

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakelauer.baseballtheater.experiences.nux.NuxActivity
import com.jakelauer.baseballtheater.utils.PrefUtils

/**
 * Created by Jake on 1/21/2017.
 */

class SplashActivity : AppCompatActivity()
{

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		val seenNux = PrefUtils.getBoolean(this, PrefUtils.SEEN_NUX)
		val activityToStart = if (seenNux) MainActivity::class.java else NuxActivity::class.java

		val intent = Intent(this, activityToStart)
		startActivity(intent)
		finish()
	}
}
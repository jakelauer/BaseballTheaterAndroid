package com.jakelauer.baseballtheater

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.OptionsProvider



/**
 * Created by Jake on 10/27/2017.
 */

internal class CastOptionsProvider : OptionsProvider
{
	override fun getCastOptions(context: Context): CastOptions
	{
		return CastOptions.Builder()
				.setReceiverApplicationId(context.getString(R.string.app_id))
				.build()
	}

	override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>?
	{
		return null
	}
}
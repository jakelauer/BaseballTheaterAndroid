package com.jakelauer.baseballtheater.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v4.content.ContextCompat
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.Pitch
import com.jakelauer.baseballtheater.R


/**
 * Created by Jake on 10/29/2017.
 */

class Utils
{
	companion object
	{
		fun isWifiAvailable(context: Context): Boolean
		{
			var cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
			var ni = cm.activeNetworkInfo

			val br = null != ni && ni.isConnected && ni.type == ConnectivityManager.TYPE_WIFI

			return br
		}

		fun dpToPx(dp: Int, context: Context): Int
		{
			val scale = context.resources.displayMetrics.density
			return (dp * scale + 0.5f).toInt()
		}

		fun getColorFromPitchResult(pitch: Pitch, context: Context): Int?
		{
			return when (pitch.type)
			{
				"B" -> ContextCompat.getColor(context, R.color.pitch_result_ball)
				"S" -> ContextCompat.getColor(context, R.color.pitch_result_strike)
				"X" -> ContextCompat.getColor(context, R.color.pitch_result_x)
				else -> null
			}
		}
	}
}
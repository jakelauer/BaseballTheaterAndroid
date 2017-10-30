package com.jakelauer.baseballtheater.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


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
	}
}
package com.jakelauer.baseballtheater.utils

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.os.Build
import android.support.v4.content.ContextCompat


/**
 * Created by Jake on 10/27/2017.
 */

class Permissions
{
	companion object
	{
		fun hasPermissions(context: Context, permissions: Array<String>): Boolean
		{
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				for (permission in permissions)
				{
					val granted = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
					if (!granted)
					{
						return false
					}
				}
			}
			return true
		}
	}
}
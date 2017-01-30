package com.jakelauer.baseballtheater;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

/**
 * Created by Jake on 1/25/2017.
 */

public class Utility {
	private static final float mScale = context.getResources().getDisplayMetrics().density;

	public static int getPixels(int dps){
		return (int) (dps * mScale + 0.5f);
	}

	public static void bold(TextView textView){
		textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
	}

	public static boolean isWifiAvailable (Context context)
	{
		boolean br = false;
		ConnectivityManager cm = null;
		NetworkInfo ni = null;

		cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		ni = cm.getActiveNetworkInfo();
		br = ((null != ni) && (ni.isConnected()) && (ni.getType() == ConnectivityManager.TYPE_WIFI));

		return br;
	}
}

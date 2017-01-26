package com.jakelauer.baseballtheater;

import android.graphics.Typeface;
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
}

package com.jakelauer.baseballtheater;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.joda.time.DateTime;

import java.util.Date;

import okhttp3.OkHttpClient;

/**
 * Created by Jake on 1/21/2017.
 */

public class BaseballTheater extends Application {
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
	}
}

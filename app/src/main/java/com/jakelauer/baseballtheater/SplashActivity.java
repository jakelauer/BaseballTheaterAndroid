package com.jakelauer.baseballtheater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.jakelauer.baseballtheater.GameList.GameListActivity;

/**
 * Created by Jake on 1/21/2017.
 */

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		determineDeviceSize();

		Intent intent = new Intent(this, GameListActivity.class);
		startActivity(intent);
		finish();
	}

	private void determineDeviceSize(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;
		Baseballtheater.setIsSmallDevice(width < 1000);
	}
}
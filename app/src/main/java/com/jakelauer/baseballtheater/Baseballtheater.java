package com.jakelauer.baseballtheater;

import android.app.Application;

import java.util.Date;

/**
 * Created by Jake on 1/21/2017.
 */

public class Baseballtheater extends Application {
	private Date gameListDate;
	private static Boolean mSmallDevice = false;
	private static Boolean mSettingsChanged = false;

	public Date getGameListDate(){
		return gameListDate;
	}

	public void setGameListDate(Date newGameListDate){
		gameListDate = newGameListDate;
	}

	public static Boolean isSmallDevice(){
		return mSmallDevice;
	}

	public static void setIsSmallDevice(Boolean isSmallDevice){
		mSmallDevice = isSmallDevice;
	}

	public static Boolean getSettingsChanged(){
		return mSettingsChanged;
	}

	public static void setSettingsChanged(Boolean settingsChanged){
		mSettingsChanged = settingsChanged;
	}
}

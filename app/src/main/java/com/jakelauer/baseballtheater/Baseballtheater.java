package com.jakelauer.baseballtheater;

import android.app.Application;

import java.util.Date;

/**
 * Created by Jake on 1/21/2017.
 */

public class BaseballTheater extends Application {
	private Date gameListDate;
	private static Boolean mSmallDevice = false;

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
}

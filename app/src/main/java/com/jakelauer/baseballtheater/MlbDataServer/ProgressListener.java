package com.jakelauer.baseballtheater.MlbDataServer;

/**
 * Created by Jake on 1/20/2017.
 */

public interface ProgressListener<T>
{
	void onProgressFinished(T data);
}
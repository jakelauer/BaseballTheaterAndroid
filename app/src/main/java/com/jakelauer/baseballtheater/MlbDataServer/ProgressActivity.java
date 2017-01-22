package com.jakelauer.baseballtheater.MlbDataServer;

/**
 * Created by Jake on 1/20/2017.
 */

public interface ProgressActivity<T> {
	void onProgressUpdate(double progress);
	void onProgressFinished(T objectInstance);
}

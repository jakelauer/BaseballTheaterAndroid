package com.jakelauer.baseballtheater.MlbDataServer.Utils;

public interface DownloadListener<T>
{
    void onDownloadComplete(T response);
    void onDownloadProgress(Double progress);
}

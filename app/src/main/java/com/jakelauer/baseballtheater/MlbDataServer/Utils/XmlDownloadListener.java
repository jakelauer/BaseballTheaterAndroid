package com.jakelauer.baseballtheater.MlbDataServer.Utils;

public interface XmlDownloadListener{
    void onXmlDownloadComplete(String response);
    void onXmlDownloadProgress(Double progress);
}

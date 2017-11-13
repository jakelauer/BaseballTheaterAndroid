package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class JsonLoader
{
    public void GetJson(String url, final DownloadListener<JSONObject> downloadListener)
    {
        JsonDownloader jsonDownloader = new JsonDownloader(new DownloadListener<String>()
        {
            @Override
            public void onDownloadComplete(String response)
            {
                Serializer serializer = new Persister();

                if (response != null && !response.isEmpty())
                {
                    try
                    {
                        JSONObject json = new JSONObject(response);
                        downloadListener.onDownloadComplete(json);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    downloadListener.onDownloadComplete(null);
                }
            }
        });

        jsonDownloader.execute(url);
    }
}


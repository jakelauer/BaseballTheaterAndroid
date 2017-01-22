package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XmlLoader<T> {
    public void GetXml(String url, final ProgressActivity progressActivity, final Class<T> classType) {
        XmlDownloader xmlDownloader = new XmlDownloader(new XmlDownloadListener() {
            @Override
            public void onXmlDownloadComplete(String response) {
                Serializer serializer = new Persister();

                try {
                    progressActivity.onProgressFinished(serializer.read(classType, response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

			@Override
            public void onXmlDownloadProgress(Double progress) {
				progressActivity.onProgressUpdate(progress);
            }
        });

        xmlDownloader.execute(url);
    }
}


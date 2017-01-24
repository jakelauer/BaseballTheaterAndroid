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

                if (response != null && !response.isEmpty()) {
                    try {
                        T objInstance = serializer.read(classType, response);
                        progressActivity.onProgressFinished(objInstance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progressActivity.onProgressFinished(null);
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


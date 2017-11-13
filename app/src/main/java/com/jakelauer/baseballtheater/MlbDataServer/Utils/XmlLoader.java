package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import com.jakelauer.baseballtheater.MlbDataServer.ProgressListener;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XmlLoader<T>
{
	public void GetXml(String url, final ProgressListener progressListener, final Class<T> classType)
	{
		XmlDownloader xmlDownloader = new XmlDownloader(new DownloadListener<String>()
		{
			@Override
			public void onDownloadComplete(String response)
			{
				Serializer serializer = new Persister();

				if (response != null && !response.isEmpty())
				{
					try
					{
						T objInstance = serializer.read(classType, response);
						progressListener.onProgressFinished(objInstance);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				} else
				{
					progressListener.onProgressFinished(null);
				}
			}
		});

		xmlDownloader.execute(url);
	}
}


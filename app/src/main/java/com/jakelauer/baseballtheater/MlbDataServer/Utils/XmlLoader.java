package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.concurrent.ExecutionException;

public class XmlLoader<T> {
    public T GetXml(String url, Class<T> typeClass) {
        XmlDownloader xmlDownloader = new XmlDownloader();
        String xmlString = null;
        try {
            xmlString = xmlDownloader.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(xmlString != null) {
            Serializer serializer = new Persister();

            try {
                return serializer.read(typeClass, xmlString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

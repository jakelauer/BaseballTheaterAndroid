package com.jakelauer.baseballtheater.MlbDataServer;

import android.os.AsyncTask;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

class XmlDownloader extends AsyncTask<String, Integer, String>
{

    @Override
    protected String doInBackground(String... params) {
        URL u;
        InputStream is = null;

        try {
            u = new URL(params[0]);
            is = u.openStream();

            String xmlString = this.getStringFromInputStream(is);

            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(is != null) {
                is.close();
            }
        } catch (IOException ioe) {
            // just going to ignore this one
        }

        return null;
    }

    private String getStringFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}

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

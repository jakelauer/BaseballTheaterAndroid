package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Jake on 1/14/2017.
 */

public class XmlDownloader extends AsyncTask<String, Integer, String>
{
    public DownloadListener mListener;

    public XmlDownloader(DownloadListener listener)
    {
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params)
    {
        URL u;
        InputStream is = null;

        String xmlString = "";
        try
        {
            u = new URL(params[0]);

            is = u.openStream();
            xmlString = this.getStringFromInputStream(is);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            if (is != null)
            {
                is.close();
            }
        } catch (IOException ioe)
        {
            // just going to ignore this one
        }

        return xmlString;
    }

    private String getStringFromInputStream(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1)
        {
            result.write(buffer, 0, length);
            onProgressUpdate(length);
        }
        return result.toString("UTF-8");
    }

    protected void onPostExecute(String xmlString)
    {
        mListener.onDownloadComplete(xmlString);
    }
}


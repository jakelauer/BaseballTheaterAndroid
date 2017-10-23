package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import android.os.AsyncTask;

import com.jakelauer.baseballtheater.MlbDataServer.ProgressListener;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class AsyncXmlParser extends AsyncTask<String, Integer, Object>
{
    private ProgressListener m_progressListener;
    private Class m_typeClass;

    public AsyncXmlParser(Class typeClass, ProgressListener progressListener)
    {
        m_progressListener = progressListener;
        m_typeClass = typeClass;
    }

    @Override
    protected Object doInBackground(String... params)
    {
        Serializer serializer = new Persister();

        Object objInstance = null;
        try
        {
            objInstance = serializer.read(m_typeClass, params[0]);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return objInstance;
    }

    protected void onPostExecute(Object objInstance)
    {
        m_progressListener.onProgressFinished(objInstance);
    }
}

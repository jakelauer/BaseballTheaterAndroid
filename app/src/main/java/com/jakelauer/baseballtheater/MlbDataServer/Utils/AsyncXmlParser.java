package com.jakelauer.baseballtheater.MlbDataServer.Utils;

import android.os.AsyncTask;

import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class AsyncXmlParser extends AsyncTask<String, Integer, Object>
{
    private ProgressActivity mProgressActivity;
    private Class mTypeClass;

    public AsyncXmlParser(Class typeClass, ProgressActivity progressActivity){
		mProgressActivity = progressActivity;
        mTypeClass = typeClass;
    }

    @Override
    protected Object doInBackground(String... params) {
        Serializer serializer = new Persister();

        Object objInstance = null;
        try {
            objInstance = serializer.read(mTypeClass, params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

	    return objInstance;
    }

	protected void onPostExecute(Object objInstance){
		mProgressActivity.onProgressFinished(objInstance);
	}
}

package com.jakelauer.baseballtheater.HighlightList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jake on 1/25/2017.
 */

public class OpenHighlightAsyncTask extends AsyncTask<String, Integer, Boolean> {
	private ProgressDialog mProgressDialog;
	private Activity mParentActivity;
	private String mUrl;

	public OpenHighlightAsyncTask(Activity parentActivity){
		mParentActivity = parentActivity;

		mProgressDialog = new ProgressDialog(mParentActivity);
		mProgressDialog.setTitle("Loading");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		mUrl = params[0];

		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con =  (HttpURLConnection) new URL(mUrl).openConnection();
			con.setRequestMethod("HEAD");
			System.out.println(con.getResponseCode());
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result){
		mProgressDialog.dismiss();

		boolean bResponse = result;
		if (bResponse==true)
		{
			openLink(mUrl);
		}
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(mParentActivity).create();
			alertDialog.setMessage("Unable to find a video at the specified quality - please try another quality link");
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.show();
		}
	}

	private void openLink(String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		mParentActivity.startActivity(browserIntent);
	}
}

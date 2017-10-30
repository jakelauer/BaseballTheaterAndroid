package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import com.jakelauer.baseballtheater.MlbDataServer.ProgressListener
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Jake on 1/25/2017.
 */

class OpenHighlightAsyncTask(context: Context, progressListener: ProgressListener<Boolean>) : AsyncTask<String, Int, Boolean>()
{
	private lateinit var m_url: String
	private var m_progressDialog: ProgressDialog = ProgressDialog(context)
	private val m_progressListener = progressListener

	init
	{
		m_progressDialog.setTitle("Loading")
		m_progressDialog.setCancelable(false)
		m_progressDialog.show()
	}

	override fun doInBackground(vararg params: String): Boolean?
	{
		m_url = params[0]

		return try
		{
			HttpURLConnection.setFollowRedirects(false)
			val con = URL(m_url).openConnection() as HttpURLConnection
			con.requestMethod = "HEAD"
			println(con.responseCode)
			con.responseCode == HttpURLConnection.HTTP_OK
		}
		catch (e: Exception)
		{
			e.printStackTrace()
			false
		}

	}

	override fun onPostExecute(result: Boolean)
	{
		m_progressDialog.dismiss()

		m_progressListener.onProgressFinished(result)
	}
}
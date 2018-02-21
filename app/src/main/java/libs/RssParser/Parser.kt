/*
*   Copyright 2016 Marco Gomiero
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*
*/
package libs.RssParser

import android.os.AsyncTask
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by marco on 6/17/16.
 */
class Parser : AsyncTask<String, Void, String?>(), Observer
{
	private var xmlParser: XMLParser = XMLParser()

	private var onComplete: OnTaskCompleted? = null

	init
	{
		xmlParser.addObserver(this)
	}

	interface OnTaskCompleted
	{
		fun onTaskCompleted(list: ArrayList<Article>)

		fun onError()
	}

	fun onFinish(onComplete: OnTaskCompleted)
	{
		this.onComplete = onComplete
	}

	override fun doInBackground(vararg url: String): String?
	{
		var response: Response?
		val client = OkHttpClient()
		val request = Request.Builder()
				.url(url[0])
				.build()

		try
		{
			response = client.newCall(request).execute()
			return response?.body()?.string()

		}
		catch (e: IOException)
		{

			e.printStackTrace()
			onComplete?.onError()

		}

		return null
	}

	override fun onPostExecute(result: String?)
	{
		try
		{
			result?.let {
				xmlParser.parseXML(result)
				Log.i("RSS Parser ", "RSS parsed correctly!")
			}
		}
		catch (e: Exception)
		{
			e.printStackTrace()
			onComplete?.onError()
		}

	}

	@Suppress("UNCHECKED_CAST")
	override fun update(observable: Observable, data: Any)
	{
		articles = data as ArrayList<Article>
		onComplete?.onTaskCompleted(articles)

	}

	companion object
	{
		internal var articles = ArrayList<Article>()
	}

	private fun getUnsafeOkHttpClient(): OkHttpClient
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager
			{
				override fun getAcceptedIssuers(): Array<X509Certificate?>
				{
					return arrayOfNulls(0)
				}

				@Throws(CertificateException::class)
				override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>,
												authType: String)
				{
				}

				@Throws(CertificateException::class)
				override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>,
												authType: String)
				{
				}
			})

			// Install the all-trusting trust manager
			val sslContext = SSLContext.getInstance("SSL")
			sslContext.init(null, trustAllCerts, java.security.SecureRandom())
			// Create an ssl socket factory with our all-trusting manager
			val sslSocketFactory = sslContext.getSocketFactory()

			return OkHttpClient.Builder()
					.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
					.hostnameVerifier { _, _ -> true }.build()

		}
		catch (e: Exception)
		{
			throw RuntimeException(e)
		}

	}
}

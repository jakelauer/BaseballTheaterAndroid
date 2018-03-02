package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.recap

import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.Syringe
import libs.ButterKnife.bindView

/**
 * Created by Jake on 3/1/2018.
 */
class RecapFragment : BaseFragment<String>()
{
	var m_game: GameSummary by Syringe()

	var m_webView: WebView by bindView(R.id.RECAP_webview)
	var m_progress: ProgressBar by bindView(R.id.RECAP_progress)

	override fun getLayoutResourceId() = R.layout.recap_fragment

	private fun loadWebView(cid: Long?)
	{
		if (cid != null)
		{
			m_webView.webViewClient = SSLTolerentWebViewClient()
			m_webView.webChromeClient = object : WebChromeClient()
			{
				override fun onProgressChanged(view: WebView, progress: Int)
				{
					m_progress.progress = progress
					if (progress > 98)
					{
						m_progress.visibility = View.GONE
					}
				}
			}
			m_webView.settings.domStorageEnabled = true;
			m_webView.overScrollMode = WebView.OVER_SCROLL_NEVER;
			m_webView.settings.javaScriptEnabled = true
			m_webView.settings.setSupportZoom(true)
			m_webView.settings.builtInZoomControls = true
			m_webView.settings.displayZoomControls = true
			m_webView.clearCache(true)
			m_webView.clearHistory()
			m_webView.isHorizontalScrollBarEnabled = false
			m_webView.loadUrl("http://m.mlb.com/news/article/" + cid)
		}
	}

	override fun createModel(): String
	{
		return ""
	}

	override fun loadData()
	{
		val gameDetailCreator = GameDetailCreator(m_game.gameDataDirectory, false)
		gameDetailCreator.getGameCenter({ data ->
			loadWebView(data.recaps?.mlb?.url?.cid)
		})
	}

	private inner class SSLTolerentWebViewClient : WebViewClient()
	{
		override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError)
		{
			handler.proceed() // Ignore SSL certificate errors
		}
	}

	companion object
	{
		fun newInstance(game: GameSummary): RecapFragment =
				RecapFragment().apply {
					m_game = game
				}
	}
}
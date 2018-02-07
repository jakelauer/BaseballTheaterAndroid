package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.boxscore

import android.net.http.SslError
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.Syringe
import libs.ButterKnife.bindView
import org.joda.time.format.DateTimeFormat


/**
 * Created by Jake on 2/5/2018.
 */
class BoxScoreFragment : BaseFragment<String>()
{
	var m_game: GameSummary by Syringe()

	var m_webView: WebView by bindView(R.id.BOXSCORE_webview)
	var m_progress: ProgressBar by bindView(R.id.BOXSCORE_progress)

	override fun getLayoutResourceId() = R.layout.boxscore_fragment

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		val fmt = DateTimeFormat.forPattern("yyyyMMdd")
		fmt?.let {
			val dateString = fmt.print(m_game.dateObj())

			m_webView.webViewClient = SSLTolerentWebViewClient()
			m_webView.webChromeClient = object : WebChromeClient()
			{
				override fun onProgressChanged(view: WebView, progress: Int)
				{
					m_progress.progress = progress
					if(progress > 98){
						m_progress.visibility = GONE
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
			m_webView.loadUrl("http://dev.baseball.theater/game/" + dateString + "/" + m_game.gamePk + "?app=true")
		}
	}

	override fun createModel(): String
	{
		return ""
	}

	override fun loadData()
	{
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
		fun newInstance(game: GameSummary): BoxScoreFragment =
				BoxScoreFragment().apply {
					m_game = game
				}
	}
}
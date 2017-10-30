package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.syringe.syringe
import com.ohoussein.playpause.PlayPauseView
import libs.bindView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jake on 10/29/2017.
 */

class HighlightCastControlFragment : BaseFragment<Any>
{
	var m_remoteMediaClient: RemoteMediaClient? = null
	private var m_highlight by syringe<Highlight>()

	private var m_title: TextView by bindView(R.id.CAST_CONTROL_title)
	private var m_subtitle: TextView by bindView(R.id.CAST_CONTROL_subtitle)
	var m_playPause: PlayPauseView by bindView(R.id.CAST_CONTROL_play_pause)
	private var m_seek: SeekBar by bindView(R.id.CAST_CONTROL_seek)
	private var m_currentTime: TextView by bindView(R.id.CAST_CONTROL_current_time)
	private var m_durationTime: TextView by bindView(R.id.CAST_CONTROL_duration_time)

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(highlight: Highlight) : super(highlight)

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		m_remoteMediaClient = (activity as BaseActivity).m_castSession?.remoteMediaClient
	}

	override fun onDestroy()
	{
		m_remoteMediaClient?.stop()
		super.onDestroy()
	}

	override fun onBindView()
	{
		m_title.text = m_highlight.headline
		m_subtitle.text = m_highlight.blurb
		m_currentTime.text = "00:00:00"
		m_durationTime.text = getDurationFromMs(m_highlight.durationMilliseconds())

		m_remoteMediaClient?.addProgressListener({ progressMs: Long, durationMs: Long ->
			m_seek.max = durationMs.toInt()
			m_seek.progress = Math.floor(progressMs / m_highlight.durationMilliseconds().toDouble()).toInt()

			m_currentTime.text = getDurationFromMs(progressMs)
		}, 100)

		m_remoteMediaClient?.addListener(MediaClientListener())

		m_seek.setOnSeekBarChangeListener(SeekChangeListener())
		m_playPause.change(false)

		m_playPause.setOnClickListener({
			if (m_remoteMediaClient?.playerState == MediaStatus.PLAYER_STATE_PLAYING)
			{
				m_remoteMediaClient?.pause()
				m_playPause.change(true)
			}
			else
			{
				m_remoteMediaClient?.play()
				m_playPause.change(false)
			}
		})

	}

	override fun getLayoutResourceId() = R.layout.highlight_cast_control_fragment

	override fun createModel() = ""

	override fun loadData()
	{
	}

	private fun getDurationFromMs(ms: Long): String
	{
		val d = Date(ms)
		val df = SimpleDateFormat("HH:mm:ss") // HH for 0-23
		df.timeZone = TimeZone.getTimeZone("GMT")

		return df.format(d)
	}

	inner class SeekChangeListener : SeekBar.OnSeekBarChangeListener
	{
		override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean)
		{
			m_remoteMediaClient?.seek(progress.toLong())
		}

		override fun onStartTrackingTouch(p0: SeekBar?)
		{
		}

		override fun onStopTrackingTouch(p0: SeekBar?)
		{
			if (m_remoteMediaClient?.playerState != MediaStatus.PLAYER_STATE_PLAYING)
			{
				m_playPause.change(true)
			}
			else
			{
				m_playPause.change(false)
			}
		}
	}

	inner class MediaClientListener : RemoteMediaClient.Listener
	{
		override fun onPreloadStatusUpdated()
		{
		}

		override fun onSendingRemoteMediaRequest()
		{
		}

		override fun onMetadataUpdated()
		{
		}

		override fun onAdBreakStatusUpdated()
		{
		}

		override fun onStatusUpdated()
		{
			if (m_remoteMediaClient?.playerState != MediaStatus.PLAYER_STATE_PAUSED && m_remoteMediaClient?.playerState != MediaStatus.PLAYER_STATE_PLAYING)
			{
				m_playPause.isClickable = false
				m_playPause.alpha = 0.5F
			}
			else
			{
				m_playPause.isClickable = true
				m_playPause.alpha = 1F
			}
		}

		override fun onQueueStatusUpdated()
		{
		}

	}

}

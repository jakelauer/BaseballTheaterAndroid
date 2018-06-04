package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.highlights

import android.content.*
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightSearchResult
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightThumbs
import com.jakelauer.baseballtheater.MlbDataServer.ProgressListener
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.OpenHighlightAsyncTask
import com.jakelauer.baseballtheater.utils.PrefUtils
import com.jakelauer.baseballtheater.utils.PrefUtils.Companion.BEHAVIOR_HIDE_SCORES
import libs.ButterKnife.bindView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


/**
 * Created by Jake on 10/26/2017.
 */

class HighlightItem(highlight: HighlightData, val m_activity: BaseActivity)
	: AdapterChildItem<HighlightItem.HighlightData, HighlightItem.ViewHolder>(highlight)
{
	private var m_prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(m_activity)

	override fun getLayoutResId() = R.layout.highlight_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			setElevation(viewHolder)
		}

		viewHolder.m_thumbnail.loadUrl(m_data.thumb)

		val title = if (m_data.recap) context.getString(R.string.highlight_recap) else m_data.headline

		viewHolder.m_title.text = title

		if (m_data.bigblurb != null)
		{
			viewHolder.m_subtitle.text = m_data.bigblurb
		}

		val hideQualities = !m_prefs.getBoolean("behavior_show_video_quality_options", true)
		if (hideQualities)
		{
			viewHolder.m_qualityHigh.visibility = View.GONE
			viewHolder.m_qualityMid.visibility = View.GONE
			viewHolder.m_qualityLow.visibility = View.GONE
		}

		if ((m_data.recap || m_data.condensed))
		{
			viewHolder.m_title.setTypeface(viewHolder.m_title.typeface, Typeface.BOLD)

			if (PrefUtils.getBoolean(context, BEHAVIOR_HIDE_SCORES))
			{
				viewHolder.m_subtitle.visibility = View.GONE
			}
			else
			{
				viewHolder.m_subtitle.visibility = View.VISIBLE
			}
		}
		else
		{
			viewHolder.m_title.typeface = Typeface.create(viewHolder.m_title.typeface, Typeface.NORMAL)
		}

		if (m_data.m_showDate)
		{
			viewHolder.m_date.visibility = View.VISIBLE

			val fmt = DateTimeFormat.forPattern("MMM d, yyyy")
			viewHolder.m_date.text = fmt.print(m_data.date)
		}
		else
		{
			viewHolder.m_date.visibility = View.GONE
		}

		setListeners(viewHolder)
	}

	private fun setListeners(viewHolder: ViewHolder)
	{
		viewHolder.m_infoWrapper.setOnClickListener(HighlightClickListener(m_activity, m_data, getDefaultUrl()))
		viewHolder.m_infoWrapper.setOnLongClickListener(HighlightLongClickListener(getDefaultUrl()))

		viewHolder.m_qualityLow.setOnClickListener(HighlightClickListener(m_activity, m_data, m_data.video_s))
		viewHolder.m_qualityMid.setOnClickListener(HighlightClickListener(m_activity, m_data, m_data.video_m))
		viewHolder.m_qualityHigh.setOnClickListener(HighlightClickListener(m_activity, m_data, m_data.video_l))

		viewHolder.m_qualityLow.setOnLongClickListener(HighlightLongClickListener(m_data.video_s))
		viewHolder.m_qualityMid.setOnLongClickListener(HighlightLongClickListener(m_data.video_m))
		viewHolder.m_qualityHigh.setOnLongClickListener(HighlightLongClickListener(m_data.video_l))
	}

	private fun getDefaultUrl(): String
	{
		return m_data.video_m ?: m_data.video_s ?: m_data.video_l
		?: throw Exception("No urls available")
	}

	@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
	private fun setElevation(viewHolder: ViewHolder)
	{
		if (m_data.recap || m_data.condensed)
		{
			viewHolder.m_container.elevation = 20f
		}
		else
		{
			viewHolder.m_container.elevation = 4f
		}
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_container: CardView by bindView(R.id.HIGHLIGHT_container)
		var m_infoWrapper: ConstraintLayout by bindView(R.id.HIGHLIGHT_info_wrapper)
		var m_thumbnail: ImageView by bindView(R.id.HIGHLIGHT_thumbnail)
		var m_title: TextView by bindView(R.id.HIGHLIGHT_title)
		var m_subtitle: TextView by bindView(R.id.HIGHLIGHT_subtitle)
		var m_qualityLow: TextView by bindView(R.id.HIGHLIGHT_quality_low)
		var m_qualityMid: TextView by bindView(R.id.HIGHLIGHT_quality_mid)
		var m_qualityHigh: TextView by bindView(R.id.HIGHLIGHT_quality_high)
		var m_date: TextView by bindView(R.id.HIGHLIGHT_date)
	}

	class HighlightLongClickListener(val m_url: String?) : View.OnLongClickListener
	{
		override fun onLongClick(nullableView: View?): Boolean
		{
			var copied = false

			nullableView?.let {view ->
				m_url?.let {
					copyStringToClipboard(view.context, it)
					copied = true
				}
			}

			return copied
		}

		private fun copyStringToClipboard(context: Context, url: String)
		{
			val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
			val clip = ClipData.newPlainText("URL", url)
			clipboard.primaryClip = clip

			Toast.makeText(context, "Highlight URL copied!", Toast.LENGTH_LONG).show()
		}

	}

	class HighlightClickListener(val m_activity: BaseActivity, val m_highlight: HighlightData, val m_url: String?) : View.OnClickListener
	{
		override fun onClick(view: View)
		{
			val task = OpenHighlightAsyncTask(view.context, ProgressListener {
				if (it && m_url != null)
				{
					openLink(m_url, view.context)
				}
				else
				{
					val alertDialog = AlertDialog.Builder(view.context).create()
					alertDialog.setMessage("Unable to find a video at the specified quality - please try another quality link")
					alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", { dialog, _ -> dialog.dismiss() })
					alertDialog.show()
				}
			})
			task.execute(m_url)
		}

		private fun openLink(url: String, context: Context)
		{
			val sm = CastContext.getSharedInstance(context).sessionManager
			val castSession = sm.currentCastSession
			val remoteMediaClient = castSession?.remoteMediaClient
			if (remoteMediaClient != null)
			{
				val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
				metadata.putString(MediaMetadata.KEY_TITLE, m_highlight.headline)
				metadata.putString(MediaMetadata.KEY_SUBTITLE, m_highlight.blurb)

				val mediaInfo = MediaInfo.Builder(url)
						.setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
						.setContentType("videos/mp4")
						.setMetadata(metadata)
						.setStreamDuration(m_highlight.durationMilliseconds)
						.build()

				remoteMediaClient.load(mediaInfo)

				val controlFragment = HighlightCastControlFragment.newInstance(m_highlight)
				controlFragment.show(m_activity.supportFragmentManager, "castControlFragment")
			}
			else
			{
				val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
				startActivity(context, browserIntent, null)
			}
		}
	}

	class HighlightData
	{
		val m_showDate: Boolean
		val recap: Boolean
		val condensed: Boolean
		val blurb: String
		val bigblurb: String
		val headline: String
		val durationMilliseconds: Long
		val thumb: String
		var video_l: String? = null
		var video_m: String? = null
		var video_s: String? = null
		var date: DateTime? = null

		constructor(showDate: Boolean, highlight: Highlight, thumbsOverride: HighlightThumbs? = null)
		{
			m_showDate = showDate
			recap = highlight.recap
			condensed = highlight.condensed
			blurb = highlight.blurb
			headline = highlight.headline
			bigblurb = highlight.bigblurb

			thumb = if (thumbsOverride != null)
			{
				thumbsOverride.med
			}
			else
			{
				val thumbs = highlight.thumbs?.thumbs ?: highlight.thumbnails
				thumbs[thumbs.size - 4]
			}

			durationMilliseconds = highlight.durationMilliseconds()

			val urls = highlight.urls ?: highlight.url

			video_l = urls[urls.size - 1]
			video_s = urls[0]
			video_m = urls[urls.size / 2]

			date = highlight.dateObj()
		}

	}
}
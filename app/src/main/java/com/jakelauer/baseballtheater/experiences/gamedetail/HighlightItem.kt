package com.jakelauer.baseballtheater.experiences.gamedetail

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.bindView

/**
 * Created by Jake on 10/26/2017.
 */

class HighlightItem(highlight: Highlight) : AdapterChildItem<Highlight, HighlightItem.ViewHolder>(highlight)
{
	override fun getLayoutResId() = R.layout.highlight_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			setElevation(viewHolder)
		}

		val thumbIndex = m_data.thumbs.thumbs.size - 4
		val thumb = m_data.thumbs.thumbs.get(thumbIndex)
		viewHolder.m_thumbnail.loadUrl(thumb)

		val title = if (m_data.recap) context.getString(R.string.highlight_recap) else m_data.headline

		viewHolder.m_title.text = title

		if (m_data.bigblurb != null)
		{
			viewHolder.m_subtitle.text = m_data.bigblurb
		}
	}

	@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
	fun setElevation(viewHolder: ViewHolder)
	{
		if (m_data.recap || m_data.condensed)
		{
			viewHolder.m_container.elevation = 20f
		}

	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_container: CardView by bindView(R.id.HIGHLIGHT_container)
		var m_thumbnail: ImageView by bindView(R.id.HIGHLIGHT_thumbnail)
		var m_title: TextView by bindView(R.id.HIGHLIGHT_title)
		var m_subtitle: TextView by bindView(R.id.HIGHLIGHT_subtitle)
	}
}
package com.jakelauer.baseballtheater.common.listitems

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.bindView

/**
 * Created by Jake on 10/26/2017.
 */

class EmptyListIndicator(model: EmptyListIndicator.Model)
	: AdapterChildItem<EmptyListIndicator.Model, EmptyListIndicator.ViewHolder>(model)
{

	override fun getLayoutResId() = R.layout.empty_list_indicator_layout

	override fun createViewHolder(view: View): ViewHolder = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_message.text = m_data.m_message
		viewHolder.m_icon.setBackgroundResource(m_data.m_icon)
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_message: TextView by bindView(R.id.EMPTY_LIST_INDICATOR_message)
		var m_icon: ImageView by bindView(R.id.EMPTY_LIST_INDICATOR_icon)
	}

	class Model(context: Context,
				@StringRes message: Int = R.string.game_list_empty,
				@DrawableRes icon: Int = R.drawable.ic_calendar_remove)
	{
		var m_message: String = context.getString(message)

		@DrawableRes
		var m_icon: Int = icon
	}
}
package com.jakelauer.baseballtheater.common.listitems

import android.content.Context
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.bindView

/**
 * Created by Jake on 10/26/2017.
 */

class EmptyListIndicator : AdapterChildItem<String?, EmptyListIndicator.ViewHolder>
{
	constructor(context: Context, @StringRes message: Int = R.string.game_list_empty) : super(null)
	{
		setData(context.getString(message))
	}

	constructor(message: String) : super(message)
	{
		setData(message)
	}

	override fun getLayoutResId() = R.layout.empty_list_indicator_layout

	override fun createViewHolder(view: View): ViewHolder = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_message.setText(m_data)
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_message: TextView by bindView(R.id.EMPTY_LIST_INDICATOR_message)
	}
}
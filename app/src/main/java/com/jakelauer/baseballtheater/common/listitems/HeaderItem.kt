package com.jakelauer.baseballtheater.common.listitems

import android.content.Context
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView

/**
 * Created by Jake on 10/27/2017.
 */
class HeaderItem : AdapterChildItem<String, HeaderItem.ViewHolder>
{
	constructor(title: String): super(title)
	constructor(@StringRes title: Int, context: Context) : super(context.getString(title))

	override fun getLayoutResId() = R.layout.header_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_header.text = m_data
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_header: TextView by bindView(R.id.HEADER_item)
	}
}
package com.jakelauer.baseballtheater.experiences.search

import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView

/**
 * Created by Jake on 3/4/2018.
 */
class LoadMoreItem(val m_buttonClickListener: View.OnClickListener) : AdapterChildItem<Any, LoadMoreItem.ViewHolder>(0)
{
	override fun getLayoutResId() = R.layout.load_more_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_button.setOnClickListener(m_buttonClickListener)
	}

	inner class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_button: CardView by bindView(R.id.LOAD_MORE_button)
	}
}
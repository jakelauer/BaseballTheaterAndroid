package com.jakelauer.baseballtheater.experiences.search

import android.content.Context
import android.view.View
import android.widget.TextView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView

/**
 * Created by Jake on 2/22/2018.
 */
class SearchEmptyItem(searchTermSpecified: Boolean) : AdapterChildItem<Boolean, SearchEmptyItem.ViewHolder>(searchTermSpecified)
{
	override fun getLayoutResId() = R.layout.search_empty_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_label.text =
				if(m_data) context.getString(R.string.search_empty_none_found)
				else context.getString(R.string.search_empty)
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_label: TextView by bindView(R.id.SEARCH_EMPTY_text)
	}
}
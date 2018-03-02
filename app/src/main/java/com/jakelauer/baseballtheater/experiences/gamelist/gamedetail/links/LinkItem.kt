package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.links

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.View
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.lucasurbas.listitemview.ListItemView
import libs.ButterKnife.bindView

/**
 * Created by jlauer on 3/1/2018.
 */
class LinkItem(val m_title: String, @DrawableRes val m_icon: Int, @ColorInt val m_iconColor: Int? = null) : AdapterChildItem<Any, LinkItem.ViewHolder>("")
{
	override fun getLayoutResId() = R.layout.link_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_listItem.setIconResId(m_icon)
		viewHolder.m_listItem.title = m_title

		m_iconColor?.let {
			viewHolder.m_listItem.iconColor = it
		}
	}

	inner class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_listItem: ListItemView by bindView(R.id.LINK_ITEM_list_item)
	}
}
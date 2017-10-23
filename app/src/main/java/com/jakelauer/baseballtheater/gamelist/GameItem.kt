package com.jakelauer.baseballtheater.gamelist

import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder

/**
 * Created by Jake on 10/22/2017.
 */
class GameItem(model: GameItem.Model) : AdapterChildItem<GameItem.Model, GameItem.ViewHolder>(model)
{
	override fun getLayoutResId(): Int
	{
		return R.layout.game_item
	}

	override fun createViewHolder(view: View): ViewHolder
	{
		return ViewHolder(view)
	}

	override fun onBindView()
	{
		m_viewHolder?.m_title?.text = m_data.m_title
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		@BindView(R.id.game_item_title)
		@JvmField
		var m_title: TextView? = null
	}

	class Model(title: String)
	{
		val m_title: String = title
	}
}
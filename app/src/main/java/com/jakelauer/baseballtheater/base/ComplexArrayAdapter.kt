package com.jakelauer.baseballtheater.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

/**
 * Created by jlauer on 2/14/2018.
 */
class ComplexArrayAdapter<TData : AdapterChildItem<*, TViewHolder>, TViewHolder : ItemViewHolder> : ArrayAdapter<TData>
{
	val m_inflater: LayoutInflater
	val m_items: List<TData>

	constructor(context: Context, items: List<TData>) : super(context, 0, items)
	{
		m_inflater = LayoutInflater.from(context)
		m_items = items
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View?
	{
		var view = convertView

		if (parent != null)
		{
			val item = m_items[position]
			view = m_inflater.inflate(item.getLayoutResId(), null)
			val viewHolder = item.createViewHolder(view)
			item.bindView(viewHolder)

			viewHolder.itemView.setOnClickListener {
				item.m_clickListener?.invoke(viewHolder.itemView, position)
			}
		}

		return view
	}
}
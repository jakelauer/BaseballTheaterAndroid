package com.jakelauer.baseballtheater.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * Created by Jake on 10/21/2017.
 */

class ComplexRecyclerAdapter : RecyclerView.Adapter<ItemViewHolder>
{
	var m_currentViewType = 0
	val m_items: MutableList<AdapterItem> = ArrayList()
	val m_classToViewtype: HashMap<Class<*>, Int> = HashMap()
	val m_typeToLayout: HashMap<Int, Int> = HashMap()
	val m_inflater: LayoutInflater

	constructor(context: Context) : super()
	{
		m_inflater = LayoutInflater.from(context)
		setHasStableIds(false)
	}

	override fun getItemCount(): Int
	{
		return m_items.size
	}

	override fun getItemViewType(position: Int): Int
	{
		val item = m_items[position]
		val viewType = m_classToViewtype[item.javaClass]
		return viewType ?: -1
	}

	override fun onBindViewHolder(holder: ItemViewHolder, position: Int)
	{
		val item = m_items[position]
		val itemViewHolder = item.createViewHolder(holder.itemView)
		item.bindView(itemViewHolder)

		holder.itemView.setOnClickListener {
			item.m_clickListener?.invoke(holder.itemView, position)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder
	{
		val layoutId = m_typeToLayout[viewType]

		layoutId?.let {
			val view = LayoutInflater.from(parent.context).inflate(it, parent, false)
			val vh = ItemViewHolder(view)
			return vh
		}

		throw Exception("Error creating ViewHolder")
	}

	fun add(item: AdapterChildItem<*, *>)
	{
		m_classToViewtype.put(item.javaClass, m_currentViewType)
		m_typeToLayout.put(m_currentViewType, item.getLayoutResId())
		m_currentViewType++

		@Suppress("UNCHECKED_CAST")
		m_items.add(item as AdapterChildItem<*, ItemViewHolder>)

		notifyDataSetChanged()
	}

	fun removeAt(position: Int)
	{
		m_items.removeAt(position)
		notifyItemRemoved(position)
	}

	fun clear()
	{
		m_classToViewtype.clear()
		m_items.clear()
		notifyDataSetChanged()
	}

	fun getItemPosition(item: AdapterChildItem<*, *>): Int
	{
		return m_items.indexOf(item)
	}
}

typealias AdapterItem = AdapterChildItem<*, ItemViewHolder>

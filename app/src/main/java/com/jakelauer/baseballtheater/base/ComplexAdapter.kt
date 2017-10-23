package com.jakelauer.baseballtheater.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakelauer.baseballtheater.R


/**
 * Created by Jake on 10/21/2017.
 */

class ComplexAdapter : RecyclerView.Adapter<ItemViewHolder>
{
    val m_items: MutableList<AdapterChildItem<*, *>> = ArrayList()
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
        return position
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int)
    {
        val item = m_items.get(position)
        item.bindView(holder.itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder
    {
        val item = m_items.get(viewType)
        val view = LayoutInflater.from(parent?.context).inflate(item.getLayoutResId(), null, false)
        return item.setViewHolder(view)
    }

    fun add(item: AdapterChildItem<*, *>)
    {
        m_items.add(item)
        notifyDataSetChanged()
    }

    fun clear()
    {
        m_items.clear()
        notifyDataSetChanged()
    }
}
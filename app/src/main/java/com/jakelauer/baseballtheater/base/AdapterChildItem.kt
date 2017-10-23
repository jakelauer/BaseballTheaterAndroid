package com.jakelauer.baseballtheater.base

import android.support.annotation.LayoutRes
import android.view.View
import butterknife.ButterKnife

/**
 * Created by Jake on 10/21/2017.
 */

abstract class AdapterChildItem<TData, TViewHolder : ItemViewHolder>(data: TData)
{
    val m_data = data
    var m_viewHolder: TViewHolder? = null
        private set

    @LayoutRes
    abstract fun getLayoutResId(): Int

    fun setViewHolder(view: View): TViewHolder
    {
        m_viewHolder = createViewHolder(view)
        return m_viewHolder!!
    }

    abstract fun createViewHolder(view: View): TViewHolder

    fun bindView(view: View)
    {
        ButterKnife.bind(m_viewHolder!!, view)
        onBindView()
    }

    abstract fun onBindView()
}
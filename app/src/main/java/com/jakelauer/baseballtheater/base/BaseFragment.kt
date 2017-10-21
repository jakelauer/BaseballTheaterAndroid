package com.jakelauer.baseballtheater.base

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseFragment : Fragment()
{
    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    var m_viewHolder: ItemViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view = inflater!!.inflate(getLayoutResourceId(), container, false)

        m_viewHolder = getViewHolder(view)
        onBindView()

        return view
    }

    protected abstract fun onBindView()

    protected abstract fun getViewHolder(view: View): ItemViewHolder
}
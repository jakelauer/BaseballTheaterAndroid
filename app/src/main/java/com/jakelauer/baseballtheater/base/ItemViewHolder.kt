package com.jakelauer.baseballtheater.base

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife

/**
 * Created by Jake on 10/20/2017.
 */

open class ItemViewHolder : RecyclerView.ViewHolder
{
    constructor(view: View) : super(view)
    {
        ButterKnife.bind(view)
    }
}
package com.jakelauer.baseballtheater.base

import android.view.View
import butterknife.ButterKnife

/**
 * Created by Jake on 10/20/2017.
 */

abstract class ItemViewHolder
{
    constructor(view: View)
    {
        ButterKnife.bind(view)
    }
}
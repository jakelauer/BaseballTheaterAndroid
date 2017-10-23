package com.jakelauer.baseballtheater.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.jakelauer.baseballtheater.R

/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseFragment<TData> : Fragment()
{
    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    var m_model: TData = createModel()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view = inflater!!.inflate(getLayoutResourceId(), container, false)

        ButterKnife.bind(this, view)
        beforeBindView()
        onBindView()

        return view
    }

    protected abstract fun createModel() : TData

    protected abstract fun onBindView()

    protected abstract fun loadData()

    open protected fun beforeBindView()
    {

    }
}
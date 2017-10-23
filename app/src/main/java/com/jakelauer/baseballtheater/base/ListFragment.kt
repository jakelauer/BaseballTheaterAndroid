package com.jakelauer.baseballtheater.base

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.jakelauer.baseballtheater.R

/**
 * Created by Jake on 10/20/2017.
 */

abstract class ListFragment<TData> : BaseFragment<TData>()
{
    @BindView(R.id.common_list)
    @JvmField
    var m_parentList: RecyclerView? = null

    var m_adapter: ComplexAdapter? = null

    protected fun createAdapter()
    {
        m_adapter = ComplexAdapter(context)
    }

    override fun beforeBindView()
    {
        createAdapter()
        if (m_parentList != null)
        {
            m_parentList?.adapter = m_adapter
            m_parentList?.layoutManager = LinearLayoutManager(context)
        }
        else
        {
            throw Exception("You must include layout 'common_list' in your Fragment layout")
        }
    }
}
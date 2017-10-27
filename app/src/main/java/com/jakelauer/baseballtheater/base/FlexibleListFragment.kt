package com.jakelauer.baseballtheater.base

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakelauer.baseballtheater.R
import libs.bindView

/**
 * Created by Jake on 10/20/2017.
 */

abstract class FlexibleListFragment<TData : Any> : BaseFragment<TData>()
{
	val m_parentList: RecyclerView by bindView(R.id.common_list)

	var m_adapter: ComplexAdapter? = null

	protected fun createAdapter()
	{
		m_adapter = ComplexAdapter(context)
	}

	override fun beforeOnBindView()
	{
		createAdapter()
		m_parentList.adapter = m_adapter
		m_parentList.layoutManager = LinearLayoutManager(context)
	}
}
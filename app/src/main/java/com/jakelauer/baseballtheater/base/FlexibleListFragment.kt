package com.jakelauer.baseballtheater.base

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakelauer.baseballtheater.R
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import libs.ButterKnife.bindView


/**
 * Created by Jake on 10/20/2017.
 */

abstract class FlexibleListFragment<out TData : Any> : BaseFragment<TData>()
{
	val m_parentList: RecyclerView by bindView(R.id.common_list)

	var m_adapter: ComplexAdapter? = null

	private fun createAdapter()
	{
		val context = context
		if (context != null)
		{
			m_adapter = ComplexAdapter(context)
		}
		else
		{
			throw Exception("Context is null")
		}
	}

	override fun beforeOnBindView()
	{
		createAdapter()
		m_parentList.adapter = m_adapter
		m_parentList.layoutManager = LinearLayoutManager(context)

		m_parentList.itemAnimator = SlideInRightAnimator()
	}
}
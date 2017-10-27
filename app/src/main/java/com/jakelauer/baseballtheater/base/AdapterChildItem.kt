package com.jakelauer.baseballtheater.base

import android.support.annotation.LayoutRes
import android.view.View

/**
 * Created by Jake on 10/21/2017.
 */

abstract class AdapterChildItem<TData, TViewHolder : ItemViewHolder>(data: TData)
{
	var m_data = data
		private set

	@LayoutRes
	abstract fun getLayoutResId(): Int

	abstract fun createViewHolder(view: View): TViewHolder

	fun bindView(viewHolder: TViewHolder)
	{
		viewHolder.let {
			//ButterKnife.bind(it, it.itemView)
			onBindView(it)
		}
	}

	protected fun setData(data: TData)
	{
		m_data = data
	}

	abstract fun onBindView(viewHolder: TViewHolder)
}
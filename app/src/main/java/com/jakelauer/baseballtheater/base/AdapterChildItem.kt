package com.jakelauer.baseballtheater.base

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by Jake on 10/21/2017.
 */

abstract class AdapterChildItem<TData, TViewHolder : ItemViewHolder>(data: TData)
{
	var m_data = data
		private set

	var m_clickListener: ItemClickListener? = null

	@LayoutRes
	abstract fun getLayoutResId(): Int

	abstract fun createViewHolder(view: View): TViewHolder

	fun bindView(viewHolder: TViewHolder)
	{
		viewHolder.let {
			onBindView(it, it.itemView.context)
		}
	}

	fun setClickListener(listener: ItemClickListener)
	{
		m_clickListener = listener
	}

	abstract fun onBindView(viewHolder: TViewHolder, context: Context)

	fun ImageView.loadUrl(url: String)
	{
		Picasso.with(context)
				.load(url)
				.resize(200, 200)
				.centerCrop()
				.into(this)
	}
}

typealias ItemClickListener = (view: View, position: Int) -> Unit
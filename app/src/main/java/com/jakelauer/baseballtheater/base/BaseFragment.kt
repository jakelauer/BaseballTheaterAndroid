package com.jakelauer.baseballtheater.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakelauer.baseballtheater.base.syringe.syringe
import icepick.Icepick

/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseFragment<TData : Any> : DialogFragment
{
	companion object
	{
		val INJECT_MEMBER_PREFIX = "m_"
	}

	@LayoutRes
	abstract fun getLayoutResourceId(): Int

	private lateinit var m_model: TData

	constructor() : super()

	constructor(vararg argList: Any)
	{
		syringe.inject(this, *argList)
	}

	fun getModel() = m_model

	override fun onCreate(savedInstanceState: Bundle?)
	{
		Icepick.restoreInstanceState(this, savedInstanceState)
		super.onCreate(savedInstanceState)

		m_model = createModel()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
	{
		var view = inflater.inflate(getLayoutResourceId(), container, false)

		return view
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		beforeOnBindView()
		onBindView()

		loadData()
	}

	protected abstract fun createModel(): TData

	protected open fun onBindView()
	{

	}

	protected abstract fun loadData()

	open protected fun beforeOnBindView()
	{

	}
}
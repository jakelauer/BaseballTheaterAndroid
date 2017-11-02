package com.jakelauer.baseballtheater.experiences.profiling

import android.annotation.SuppressLint
import android.util.Log
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.syringe.syringe
import org.joda.time.DateTime

/**
 * Created by jlauer on 10/31/2017.
 */
class ProfilingFragmentSyringe : BaseFragment<String>
{
	var m_arg1: String by syringe()
	var m_arg2: DateTime by syringe()
	var m_arg3: Int by syringe()
	var m_arg4: Byte by syringe()
	var m_arg5: Long by syringe()

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(arg1: String, arg2: DateTime, arg3: Int, arg4: Byte, arg5: Long) : super(arg1, arg2, arg3, arg4, arg5)

	override fun getLayoutResourceId() = R.layout.common_list

	override fun createModel(): String = ""

	override fun loadData()
	{
	}

	override fun onBindView()
	{
		Log.d("TIME_ON_BIND_VIEW", System.nanoTime().toString())
/*
		Log.d("arg1", m_arg1)
		Log.d("arg2", m_arg2.toString())
		Log.d("arg3", m_arg3.toString())
		Log.d("arg4", m_arg4.toString())
		Log.d("arg5", m_arg5.toString())*/
	}
}
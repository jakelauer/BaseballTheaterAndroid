package com.jakelauer.baseballtheater.experiences.profiling

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import org.joda.time.DateTime

/**
 * Created by jlauer on 10/31/2017.
 */
class ProfilingFragmentDefault() : BaseFragment<String>()
{
	var m_arg1: String? = null
	var m_arg2: DateTime? = null
	var m_arg3: Int? = null
	var m_arg4: Byte? = null
	var m_arg5: Long? = null

	companion object
	{
		const val ARG_1 = "arg1"
		const val ARG_2 = "arg2"
		const val ARG_3 = "arg3"
		const val ARG_4 = "arg4"
		const val ARG_5 = "arg5"

		fun newInstance(arg1: String, arg2: DateTime, arg3: Int, arg4: Byte, arg5: Long) : ProfilingFragmentDefault
		{
			val startTime = System.nanoTime()

			val args = Bundle()
			args.putString(ARG_1, arg1)
			args.putSerializable(ARG_2, arg2)
			args.putInt(ARG_3, arg3)
			args.putByte(ARG_4, arg4)
			args.putLong(ARG_5, arg5)

			val fragment = ProfilingFragmentDefault()
			fragment.arguments = args

			val endTime = System.nanoTime()
			Log.d("TOTAL_TIME", ((endTime - startTime).toDouble()/1000000.0).toString())
			return fragment
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
	{
		val view = super.onCreateView(inflater, container, savedInstanceState)

		val args = arguments
		m_arg1 = args.getString(ARG_1)
		m_arg2 = args.getSerializable(ARG_2) as DateTime
		m_arg3 = args.getInt(ARG_3)
		m_arg4 = args.getByte(ARG_4)
		m_arg5 = args.getLong(ARG_5)

		return view
	}

	override fun getLayoutResourceId() = R.layout.common_list

	override fun createModel(): String = ""

	override fun loadData()
	{
	}

	override fun onBindView()
	{/*
		Log.d("arg1", m_arg1)
		Log.d("arg2", m_arg2.toString())
		Log.d("arg3", m_arg3.toString())
		Log.d("arg4", m_arg4.toString())
		Log.d("arg5", m_arg5.toString())*/
	}
}
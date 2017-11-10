package com.jakelauer.baseballtheater.experiences.nux

import android.os.Bundle
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.utils.PrefUtils

/**
 * Created by Jake on 11/8/2017.
 */
class NuxActivity : BaseActivity(false)
{
	override val m_layoutResId: Int
		get() = R.layout.nux_activity

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		PrefUtils.set(this, PrefUtils.SEEN_NUX, true)
	}

	override fun onBindView()
	{
		val fragment = NuxFragment()
		setMainFragment(fragment)
	}
}
package com.jakelauer.baseballtheater.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.jakelauer.baseballtheater.R
import icepick.Icepick


/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseActivity : AppCompatActivity()
{
	@get:LayoutRes
	protected abstract val layoutResId: Int

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		Icepick.restoreInstanceState(this, savedInstanceState)

		setContentView(layoutResId)

		onBindView()
	}

	protected abstract fun onBindView()

	protected fun setMainFragment(fragment: Fragment)
	{
		val fragmentManager = supportFragmentManager
		val fragmentTransaction = fragmentManager?.beginTransaction()
		fragmentTransaction?.replace(R.id.content_frame, fragment)
		fragmentTransaction?.commit()
	}

	fun setPref(key: String, value: String)
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		val editor = sharedPref.edit()
		editor.putString(key, value)
		editor.commit()
	}

	fun getPref(key: String): String
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		return sharedPref.getString(key, "")
	}

	fun clearPref(key: String)
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		val editor = sharedPref.edit()
		editor.remove(key)
	}
}

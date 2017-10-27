package com.jakelauer.baseballtheater.base

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

        //ButterKnife.bind(this)

        onBindView()
    }

    protected abstract fun onBindView()

    protected fun setMainFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.content_frame, fragment)
        fragmentTransaction?.commit()
    }
}

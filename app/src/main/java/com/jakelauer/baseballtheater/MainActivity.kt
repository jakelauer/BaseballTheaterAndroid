package com.jakelauer.baseballtheater

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.jakelauer.baseballtheater.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base.*

class MainActivity : BaseActivity()
{
    override val layoutResId: Int
        get() = R.layout.activity_base

    override fun onBindView()
    {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId)
        {
            R.id.navigation_home ->
            {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard ->
            {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}
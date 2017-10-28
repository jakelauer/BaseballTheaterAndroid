package com.jakelauer.baseballtheater

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.experiences.articles.ArticlesFragment
import com.jakelauer.baseballtheater.experiences.gamelist.GameListPagerFragment
import kotlinx.android.synthetic.main.activity_base.*
import org.joda.time.DateTime
import com.google.android.gms.cast.framework.CastContext



class MainActivity : BaseActivity()
{
    override val layoutResId: Int
        get() = R.layout.activity_base

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val castContext = CastContext.getSharedInstance(this)
    }

    override fun onBindView()
    {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = GameListPagerFragment(DateTime.now())
        clearPref("date")
        setMainFragment(fragment)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId)
        {
            R.id.navigation_games ->
            {
                val dateString = getPref("date")
                val date = if(dateString != "") DateTime(dateString) else DateTime.now()
                val fragment = GameListPagerFragment(date)
                setMainFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_articles ->
            {
                val fragment = ArticlesFragment()
                setMainFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
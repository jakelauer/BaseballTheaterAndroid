package com.jakelauer.baseballtheater

import android.support.design.widget.BottomNavigationView
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.experiences.news.NewsFragment
import com.jakelauer.baseballtheater.experiences.gamelist.GameListPagerFragment
import kotlinx.android.synthetic.main.activity_base.*
import org.joda.time.DateTime


class MainActivity : BaseActivity()
{
	override val m_layoutResId: Int
		get() = R.layout.activity_base

	override fun onBindView()
	{
		navigation.setOnNavigationItemSelectedListener(m_onNavigationItemSelectedListener)
		val fragment = GameListPagerFragment(DateTime.now())
		clearPref("date")
		setMainFragment(fragment)
	}

	private val m_onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.itemId)
		{
			R.id.navigation_games ->
			{
				val dateString = getPref("date")
				val date = if (dateString != "") DateTime(dateString) else DateTime.now()
				val fragment = GameListPagerFragment(date)
				setMainFragment(fragment)
				return@OnNavigationItemSelectedListener true
			}
			R.id.navigation_news ->
			{
				val fragment = NewsFragment()
				setMainFragment(fragment)
				return@OnNavigationItemSelectedListener true
			}
		}
		false
	}
}
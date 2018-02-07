package com.jakelauer.baseballtheater

import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.experiences.gamelist.GameListPagerFragment
import com.jakelauer.baseballtheater.experiences.news.NewsFragment
import com.jakelauer.baseballtheater.utils.DateTimeUtils
import com.jakelauer.baseballtheater.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_base.*


class MainActivity : BaseActivity(true)
{
	override val m_layoutResId: Int
		get() = R.layout.activity_base

	fun resetTitle()
	{
		title = "Baseball Theater"
	}

	override fun onBindView()
	{
		PreferenceManager.setDefaultValues(this, R.xml.settings, true)

		val showNewsInOffseason = PrefUtils.getBoolean(this, PrefUtils.NEWS_SHOW_IN_OFFSEASON)
		val isOffseason = DateTimeUtils.getIsOffseason()
		val newsFirst = showNewsInOffseason && isOffseason

		val menuId = if (newsFirst) R.menu.navigation_news_first else R.menu.navigation

		navigation.inflateMenu(menuId)
		navigation.setOnNavigationItemSelectedListener(m_onNavigationItemSelectedListener)

		clearPref("date")

		val fragment: Fragment
		if (newsFirst)
		{
			fragment = NewsFragment()
			navigation.menu.findItem(R.id.navigation_news).isChecked = true
		}
		else
		{
			fragment = GameListPagerFragment()
		}
		setMainFragment(fragment)
	}

	private val m_onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.itemId)
		{
			R.id.navigation_games ->
			{
				val fragment = GameListPagerFragment()
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
package com.jakelauer.baseballtheater

import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.experiences.gamelist.GameListPagerFragment
import com.jakelauer.baseballtheater.experiences.search.SearchFragment
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

		val menuId = R.menu.navigation

		navigation.inflateMenu(menuId)
		navigation.setOnNavigationItemSelectedListener(m_onNavigationItemSelectedListener)

		clearPref("date")

		val fragment = GameListPagerFragment()
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

			R.id.navigation_search ->
			{
				val fragment = SearchFragment()
				setMainFragment(fragment)
				return@OnNavigationItemSelectedListener true
			}
		}
		false
	}
}
package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.annotation.SuppressLint
import android.graphics.Movie
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.syringe.syringe
import libs.ButterKnife.bindView

/**
 * Created by Jake on 2/5/2018.
 */

class GameDetailWrapperFragment : BaseFragment<String>
{
	var m_game: GameSummary by syringe()

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(game: GameSummary) : super(game)

	val viewPager: ViewPager by bindView(R.id.GAME_DETAIL_viewpager)
	val tabLayout: TabLayout by bindView(R.id.GAME_DETAIL_tabs)

	override fun getLayoutResourceId() = R.layout.game_detail_fragment

	override fun createModel(): String
	{
		return ""
	}

	override fun loadData()
	{
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		val adapter = GameDetailAdapter(fragmentManager)
		viewPager.adapter = adapter
		tabLayout.setupWithViewPager(viewPager)
	}

	inner class GameDetailAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)
	{
		override fun getItem(position: Int): Fragment
		{
			return when (position) {
				0 -> HighlightsFragment(m_game)
				1 -> HighlightsFragment(m_game)
				2 -> HighlightsFragment(m_game)
				else -> throw Exception("Page not found")
			}
		}

		override fun getCount() = 3

		override fun getPageTitle(position: Int): CharSequence
		{
			return when(position){
				0 -> "Highlights"
				1 -> "Play by Play"
				2 -> "Box Score"
				else -> "Not Found"
			}
		}
	}
}
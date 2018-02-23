package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.Syringe
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.boxscore.BoxScoreFragment
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.highlights.HighlightsFragment
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay.PlayByPlayFragment
import libs.ButterKnife.bindView

/**
 * Created by Jake on 2/5/2018.
 */

class GameDetailWrapperFragment : BaseFragment<String>()
{
	var m_game: GameSummary by Syringe()

	val viewPager: ViewPager by bindView(R.id.GAME_DETAIL_viewpager)
	val tabLayout: TabLayout by bindView(R.id.GAME_DETAIL_tabs)

	override fun getLayoutResourceId() = R.layout.game_detail_fragment

	override fun createModel(): String
	{
		return ""
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		activity?.title = "Game Detail"
	}

	override fun loadData()
	{
	}

	override fun onBindView()
	{
		fragmentManager?.let {
			val adapter = GameDetailAdapter(it)
			viewPager.adapter = adapter
			tabLayout.setupWithViewPager(viewPager)
		}
	}

	inner class GameDetailAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager)
	{
		override fun getItem(position: Int): Fragment
		{
			return when (position) {
				0 -> HighlightsFragment.newInstance(m_game)
				1 -> PlayByPlayFragment.newInstance(m_game)
				2 -> BoxScoreFragment.newInstance(m_game)
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

	companion object
	{
		fun newInstance(game: GameSummary): GameDetailWrapperFragment{
			return GameDetailWrapperFragment().apply {
				m_game = game
			}
		}
	}
}
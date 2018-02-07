package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.base.Syringe

/**
 * Created by Jake on 2/7/2018.
 */
class PlayByPlayFragment : RefreshableListFragment<PlayByPlayFragment.Model>()
{
	var m_game: GameSummary by Syringe()

	override fun getLayoutResourceId() = R.layout.play_by_play_fragment

	override fun createModel(): Model = Model()

	override fun loadData()
	{
	}

	inner class Model
	{

	}

	companion object
	{
		fun newInstance(game: GameSummary): PlayByPlayFragment
				= PlayByPlayFragment().apply {
			m_game = game
		}
	}
}
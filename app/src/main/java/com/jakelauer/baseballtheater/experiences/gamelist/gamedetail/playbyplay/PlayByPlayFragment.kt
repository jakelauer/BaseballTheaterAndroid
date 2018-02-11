package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.InningsGame
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.base.Syringe
import java.util.concurrent.ExecutionException

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
		m_refreshView.isRefreshing = true

		val gdc = GameDetailCreator(m_game.gameDataDirectory, false)
		try
		{
			gdc.getInnings {
				getModel().updatePlayByPlay(it)
				onDataLoaded()
			}
		}
		catch (e: Exception)
		{
			e.printStackTrace()
		}
	}

	fun onDataLoaded()
	{
		val innings = getModel().m_inningsGame
		if (innings != null)
		{

		}
	}

	inner class Model
	{
		var m_inningsGame: InningsGame? = null
			private set

		fun updatePlayByPlay(inningsGame: InningsGame)
		{
			m_inningsGame = inningsGame
		}
	}

	companion object
	{
		fun newInstance(game: GameSummary): PlayByPlayFragment = PlayByPlayFragment().apply {
			m_game = game
		}
	}
}
package com.jakelauer.baseballtheater.experiences.gamedetail

import android.os.Bundle
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.FlexibleListFragment
import com.jakelauer.baseballtheater.utils.inject

/**
 * Created by Jake on 10/25/2017.
 */
class GameDetailFragment : FlexibleListFragment<GameDetailFragment.Model>()
{
	var m_game: GameSummary by inject(ARG_GAME_SUMMARY)

	companion object
	{
		const val ARG_ITEM_ID = "gamePk"
		const val ARG_GAME_SUMMARY = "gameSummary"

		fun newInstance(game: GameSummary): GameDetailFragment
		{
			val args = Bundle()
			args.putInt(ARG_ITEM_ID, game.gamePk)
			args.putSerializable(ARG_GAME_SUMMARY, game)
			val fragment = GameDetailFragment()
			fragment.arguments = args
			return fragment
		}
	}

	override fun getLayoutResourceId() = R.layout.game_detail_fragment

	override fun createModel(): Model = Model()

	override fun loadData()
	{
	}

	class Model
	{

	}
}
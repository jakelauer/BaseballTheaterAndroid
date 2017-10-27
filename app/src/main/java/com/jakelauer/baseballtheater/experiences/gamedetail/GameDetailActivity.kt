package com.jakelauer.baseballtheater.experiences.gamedetail

import android.content.Context
import android.content.Intent
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.utils.inject

/**
 * Created by Jake on 10/25/2017.
 */
class GameDetailActivity : BaseActivity()
{
	override val layoutResId: Int
		get() = R.layout.game_detail_activity

	val m_game: GameSummary by inject(ARG_GAME_SUMMARY)

	override fun onBindView()
	{
		val fragment = GameDetailFragment.newInstance(m_game)
		setMainFragment(fragment)
	}

	companion object
	{
		const val ARG_ITEM_ID = "gamePk"
		const val ARG_GAME_SUMMARY = "gameSummary"

		fun startIntent(game: GameSummary, context: Context): Intent
		{
			val intent = Intent(context, GameDetailActivity::class.java)
			intent.putExtra(ARG_ITEM_ID, game.gamePk)
			intent.putExtra(ARG_GAME_SUMMARY, game)
			return intent
		}

		fun startActivity(game: GameSummary,
						  context: Context)
		{
			context.startActivity(startIntent(game, context))
		}
	}

}
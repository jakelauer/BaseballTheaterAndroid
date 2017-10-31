package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.content.Context
import android.content.Intent
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.syringe.syringe

/**
 * Created by Jake on 10/25/2017.
 */
class GameDetailActivity : BaseActivity()
{
	override val m_layoutResId: Int
		get() = R.layout.game_detail_activity

	val m_game: GameSummary by syringe()

	override fun onBindView()
	{
		val fragment = GameDetailFragment(m_game)
		setMainFragment(fragment)
	}

	companion object
	{
		fun startIntent(game: GameSummary, context: Context): Intent
		{
			val intent = Intent(context, GameDetailActivity::class.java)
			intent.putExtra("m_game", game)
			return intent
		}

		fun startActivity(game: GameSummary,
						  context: Context)
		{
			context.startActivity(startIntent(game, context))
		}
	}

}
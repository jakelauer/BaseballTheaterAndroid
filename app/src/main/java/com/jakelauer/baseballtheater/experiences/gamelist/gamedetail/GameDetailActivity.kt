package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity

/**
 * Created by Jake on 10/25/2017.
 */
class GameDetailActivity : BaseActivity(true)
{
	override val m_layoutResId: Int
		get() = R.layout.game_detail_activity

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		setShowBackButton(true)
	}

	override fun onBindView()
	{
		val game = intent.getSerializableExtra("m_game") as GameSummary
		val fragment = GameDetailWrapperFragment.newInstance(game)
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
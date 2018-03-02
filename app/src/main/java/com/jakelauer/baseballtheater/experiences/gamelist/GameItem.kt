package com.jakelauer.baseballtheater.experiences.gamelist

import android.content.Context
import android.graphics.Color
import android.graphics.Shader
import android.support.v7.widget.CardView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.jakelauer.baseballtheater.utils.PrefUtils
import com.jakelauer.baseballtheater.utils.PrefUtils.Companion.BEHAVIOR_HIDE_SCORES
import com.jakelauer.baseballtheater.utils.TeamColors
import libs.ButterKnife.bindView
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.ShapeDrawable
import android.graphics.LinearGradient




/**
 * Created by Jake on 10/22/2017.
 */
class GameItem(model: GameItem.Model) : AdapterChildItem<GameItem.Model, GameItem.ViewHolder>(model)
{
	override fun getLayoutResId(): Int
	{
		return R.layout.game_item
	}

	override fun createViewHolder(view: View): ViewHolder
	{
		return ViewHolder(view)
	}

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_awayTeamCity.text = m_data.m_game.awayTeamCity
		viewHolder.m_awayTeamName.text = m_data.m_game.awayTeamName
		viewHolder.m_awayTeamScore.text = m_data.m_awayTeamScore
		viewHolder.m_homeTeamCity.text = m_data.m_game.homeTeamCity
		viewHolder.m_homeTeamName.text = m_data.m_game.homeTeamName
		viewHolder.m_homeTeamScore.text = m_data.m_homeTeamScore
		viewHolder.m_gameStatus.text = m_data.m_game.currentInning

		viewHolder.m_awayTeamCity.setTextColor(TeamColors.getTeamColor(m_data.m_game.awayFileCode, viewHolder.itemView.context))
		viewHolder.m_awayTeamName.setTextColor(TeamColors.getTeamColorSecondary(m_data.m_game.awayFileCode, viewHolder.itemView.context))
		viewHolder.m_homeTeamCity.setTextColor(TeamColors.getTeamColor(m_data.m_game.homeFileCode, viewHolder.itemView.context))
		viewHolder.m_homeTeamName.setTextColor(TeamColors.getTeamColorSecondary(m_data.m_game.homeFileCode, viewHolder.itemView.context))

		if (PrefUtils.getBoolean(context, BEHAVIOR_HIDE_SCORES))
		{
			viewHolder.m_awayTeamScore.text = "▨"
			viewHolder.m_homeTeamScore.text = "▨"
		}
		else
		{
			if (m_data.m_game.gameIsOver)
			{
				val homeWon = (m_data.m_homeTeamScore.toInt() > m_data.m_awayTeamScore.toInt())

				viewHolder.m_homeTeamWon.alpha = if (homeWon) 1f else 0f
				viewHolder.m_awayTeamWon.alpha = if (!homeWon) 1f else 0f

				viewHolder.m_awayTeamWrapper.alpha = if (homeWon) 0.35F else 1F
				viewHolder.m_homeTeamWrapper.alpha = if (!homeWon) 0.35F else 1F
			}
		}

		viewHolder.m_gameItemContainer.elevation = if (m_data.m_isFavTeam) 20F else 4F
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_gameItemContainer: CardView by bindView(R.id.game_item_container)

		val m_awayTeamWrapper: FrameLayout by bindView(R.id.game_away_team_wrapper)
		val m_awayTeamCity: TextView by bindView(R.id.game_away_team_city)
		val m_awayTeamName: TextView by bindView(R.id.game_away_team_name)
		val m_awayTeamScore: TextView by bindView(R.id.game_away_team_score)
		val m_awayTeamWon: ImageView by bindView(R.id.game_away_team_won)

		val m_homeTeamWrapper: FrameLayout by bindView(R.id.game_home_team_wrapper)
		val m_homeTeamCity: TextView by bindView(R.id.game_home_team_city)
		val m_homeTeamName: TextView by bindView(R.id.game_home_team_name)
		val m_homeTeamScore: TextView by bindView(R.id.game_home_team_score)
		val m_homeTeamWon: ImageView by bindView(R.id.game_home_team_won)
		val m_gameStatus: TextView by bindView(R.id.game_status)
	}

	class Model(var m_game: GameSummary, var m_isFavTeam: Boolean)
	{
		var m_awayTeamScore: String = m_game.linescore?.runs?.away ?: ""
		var m_homeTeamScore: String = m_game.linescore?.runs?.home ?: ""
	}
}
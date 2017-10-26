package com.jakelauer.baseballtheater.gamelist

import android.view.View
import android.widget.TextView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.jakelauer.baseballtheater.utils.TeamColors
import libs.bindView

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

	override fun onBindView(viewHolder: GameItem.ViewHolder)
	{
		viewHolder.m_awayTeamCity.setText(m_data.m_game.awayTeamCity)
		viewHolder.m_awayTeamName.setText(m_data.m_game.awayTeamName)
		viewHolder.m_awayTeamScore.setText(m_data.m_awayTeamScore)
		viewHolder.m_homeTeamCity.setText(m_data.m_game.homeTeamCity)
		viewHolder.m_homeTeamName.setText(m_data.m_game.homeTeamName)
		viewHolder.m_homeTeamScore.setText(m_data.m_homeTeamScore)
		viewHolder.m_gameStatus.setText(m_data.m_game.currentInning)

		viewHolder.m_awayTeamCity.setTextColor(TeamColors.getTeamColor(m_data.m_game.awayFileCode, viewHolder.itemView.context))
		viewHolder.m_awayTeamName.setTextColor(TeamColors.getTeamColor(m_data.m_game.awayFileCode, viewHolder.itemView.context))
		viewHolder.m_homeTeamCity.setTextColor(TeamColors.getTeamColor(m_data.m_game.homeFileCode, viewHolder.itemView.context))
		viewHolder.m_homeTeamName.setTextColor(TeamColors.getTeamColor(m_data.m_game.homeFileCode, viewHolder.itemView.context))
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_awayTeamCity: TextView by bindView(R.id.game_away_team_city)
		val m_awayTeamName: TextView by bindView(R.id.game_away_team_name)
		val m_awayTeamScore: TextView by bindView(R.id.game_away_team_score)
		val m_homeTeamCity: TextView by bindView(R.id.game_home_team_city)
		val m_homeTeamName: TextView by bindView(R.id.game_home_team_name)
		val m_homeTeamScore: TextView by bindView(R.id.game_home_team_score)
		val m_gameStatus: TextView by bindView(R.id.game_status)
	}

	class Model
	{
		var m_game: GameSummary

		var m_awayTeamScore: String
		var m_homeTeamScore: String

		constructor(game: GameSummary)
		{
			m_game = game
			m_awayTeamScore = game.linescore?.runs?.away ?: ""
			m_homeTeamScore = game.linescore?.runs?.home ?: ""
		}
	}
}
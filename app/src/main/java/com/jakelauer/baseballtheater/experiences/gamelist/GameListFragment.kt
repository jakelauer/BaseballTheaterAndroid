package com.jakelauer.baseballtheater.experiences.gamelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection
import com.jakelauer.baseballtheater.MlbDataServer.GameSummaryCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.FlexibleListFragment
import com.jakelauer.baseballtheater.common.listitems.EmptyListIndicator
import com.jakelauer.baseballtheater.utils.Inject
import libs.bindView
import org.joda.time.DateTime
import java.util.concurrent.ExecutionException


/**s
 * Created by Jake on 10/20/2017.
 */
class GameListFragment : FlexibleListFragment<GameListFragment.Model>
{
	var m_date: DateTime by Inject<DateTime>()

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(date: DateTime) : super(date)

	var m_refreshView: SwipeRefreshLayout by bindView(R.id.game_list_refresh)

	override fun getLayoutResourceId(): Int = R.layout.game_list_fragment

	override fun createModel(): Model
	{
		return Model()
	}

	override fun onBindView()
	{
		m_refreshView.setOnRefreshListener {
			loadData()
		}
	}

	override fun loadData()
	{
		m_refreshView.isRefreshing = true

		val gsCreator = GameSummaryCreator()
		try
		{
			gsCreator.GetSummaryCollection(m_date) {
				getModel().updateGames(it)
				onDataLoaded()
			}
		}
		catch (e: ExecutionException)
		{
			e.printStackTrace()
		}
		catch (e: InterruptedException)
		{
			e.printStackTrace()
		}
	}

	fun onDataLoaded()
	{
		m_refreshView.isRefreshing = false
		m_adapter?.clear()

		val games = getModel().getGames()
		if (games?.GameSummaries != null)
		{
			for (game in games.GameSummaries)
			{
				val item = GameItem(GameItem.Model(game))

				m_adapter?.add(item)
			}
		}
		else
		{
			m_adapter?.add(EmptyListIndicator(context))
		}
	}

	class Model
	{
		private var m_games: GameSummaryCollection? = null

		fun updateGames(data: GameSummaryCollection?)
		{
			m_games = data
		}

		fun getGames() = m_games
	}
}
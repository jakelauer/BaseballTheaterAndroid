package com.jakelauer.baseballtheater.gamelist

import android.os.Bundle
import android.support.v4.app.Fragment
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection
import com.jakelauer.baseballtheater.MlbDataServer.GameSummaryCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.jakelauer.baseballtheater.base.ListFragment
import com.jakelauer.baseballtheater.utils.inject
import org.joda.time.DateTime
import java.util.concurrent.ExecutionException


/**s
 * Created by Jake on 10/20/2017.
 */
class GameListFragment : ListFragment<GameListFragment.Model>()
{
	var m_date: DateTime by inject(ARG_DATE)

	override fun getLayoutResourceId(): Int = R.layout.game_list_fragment

	companion object
	{
		const val ARG_DATE = "ARG_DATE"
		fun newInstance(date: DateTime): Fragment
		{
			val args = Bundle()
			args.putSerializable(ARG_DATE, date)
			val fragment = GameListFragment()
			fragment.arguments = args
			return fragment
		}
	}

	override fun createModel(): Model
	{
		return Model()
	}

	override fun loadData()
	{
		if (m_date != null)
		{
			val gsCreator = GameSummaryCreator()
			try
			{
				gsCreator.GetSummaryCollection(m_date, { data ->
					getModel().updateGames(data)
					onDataLoaded()
				})
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
	}

	fun onDataLoaded()
	{
		m_adapter?.clear()

		val games = getModel().getGames()
		if (games != null)
		{
			for (game in games.GameSummaries)
			{
				val item = GameItem(GameItem.Model(game))

				@Suppress("UNCHECKED_CAST")
				m_adapter?.add(item as AdapterChildItem<*, ItemViewHolder>)
			}
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
package com.jakelauer.baseballtheater.gamelist

import android.os.Bundle
import android.support.v4.app.Fragment
import com.f2prateek.dart.InjectExtra
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection
import com.jakelauer.baseballtheater.MlbDataServer.GameSummaryCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.ListFragment
import org.joda.time.DateTime
import java.util.concurrent.ExecutionException


/**s
 * Created by Jake on 10/20/2017.
 */

class GameListFragment : ListFragment<GameListFragment.Model>()
{

	@InjectExtra(ARG_DATE)
	var m_date: DateTime? = null

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

	override fun onBindView()
	{
		m_adapter?.clear()

		val item = GameItem(GameItem.Model("Test"))
		m_adapter?.add(item)
	}

	override fun loadData()
	{
		if (m_date != null)
		{
			val gsCreator = GameSummaryCreator()
			try
			{
				gsCreator.GetSummaryCollection(m_date, { data ->
					m_model.updateData(data)
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

	class Model
	{
		var m_data: GameSummaryCollection? = null

		fun updateData(data: GameSummaryCollection)
		{
			m_data = data
		}
	}
}
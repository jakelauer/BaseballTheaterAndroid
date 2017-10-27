package com.jakelauer.baseballtheater.experiences.gamedetail

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.FlexibleListFragment
import com.jakelauer.baseballtheater.common.listitems.HeaderItem
import com.jakelauer.baseballtheater.utils.inject
import libs.bindView
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * Created by Jake on 10/25/2017.
 */
class GameDetailFragment : FlexibleListFragment<GameDetailFragment.Model>()
{
	var m_game: GameSummary by inject(ARG_GAME_SUMMARY)

	var m_refreshView: SwipeRefreshLayout by bindView(R.id.GAME_DETAIL_refresh)

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

	override fun onBindView()
	{
		m_refreshView.setOnRefreshListener {
			loadData()
		}
	}

	override fun loadData()
	{
		m_refreshView.isRefreshing = true
		val gdc = GameDetailCreator(m_game.gameDataDirectory, false)
		try
		{
			gdc.getHighlights {
				getModel().updateHighlights(it)
				onDataLoaded()
				m_refreshView.isRefreshing = false
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
		val highlights = getModel().m_highlights
		if (highlights?.highlights != null)
		{
			Collections.sort(highlights.highlights, Comparator<Highlight> { a, b ->
				val aIsRecap = if (a.recap) -1 else 0
				val bIsRecap = if (b.recap) -1 else 0
				val aIsCondensed = if (a.condensed) -1 else 0
				val bIsCondensed = if (b.condensed) -1 else 0

				val recapResult = aIsRecap - bIsRecap
				val condensedResult = aIsCondensed - bIsCondensed
				val idResult = a.id - b.id

				if (recapResult != 0)
				{
					return@Comparator recapResult
				}

				if (condensedResult != 0)
				{
					condensedResult
				}
				else idResult
			})

			var pastSpecialHighlights = false
			for (highlight in highlights.highlights)
			{
				if (!highlight.condensed && !highlight.recap && !pastSpecialHighlights)
				{
					pastSpecialHighlights = true

					val headerItem = HeaderItem(R.string.highlight_list_title, context)
					m_adapter?.add(headerItem)
				}

				val highlightItem = HighlightItem(highlight)

				m_adapter?.add(highlightItem)
			}
		}
	}

	class Model
	{
		var m_highlights: HighlightsCollection? = null
			private set

		fun updateHighlights(highlights: HighlightsCollection?)
		{
			m_highlights = highlights
		}
	}
}
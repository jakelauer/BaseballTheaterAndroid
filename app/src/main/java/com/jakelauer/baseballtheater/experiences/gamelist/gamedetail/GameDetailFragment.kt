package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail

import android.annotation.SuppressLint
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.FlexibleListFragment
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.common.listitems.HeaderItem
import com.jakelauer.baseballtheater.base.syringe.syringe
import com.jakelauer.baseballtheater.common.listitems.EmptyListIndicator
import kotlinx.android.synthetic.main.game_detail_fragment.*
import libs.bindView
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * Created by Jake on 10/25/2017.
 */
class GameDetailFragment : RefreshableListFragment<GameDetailFragment.Model>
{
	var m_game: GameSummary by syringe()

	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(game: GameSummary) : super(game)

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

				val highlightItem = HighlightItem(highlight, activity as BaseActivity)

				m_adapter?.add(highlightItem)
			}
		}
		else
		{
			val emptyListItem = EmptyListIndicator(EmptyListIndicator.Model(context, R.string.highlight_list_empty, R.drawable.ic_videocam_off_black_24px))
			m_adapter?.add(emptyListItem)
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
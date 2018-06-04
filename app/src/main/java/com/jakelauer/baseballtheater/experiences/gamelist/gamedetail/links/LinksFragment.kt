package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.links

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.view.View
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameCenter
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.ItemClickListener
import com.jakelauer.baseballtheater.base.RefreshableListFragment
import com.jakelauer.baseballtheater.base.Syringe
import org.joda.time.format.DateTimeFormat

/**
 * Created by jlauer on 3/1/2018.
 */
class LinksFragment : RefreshableListFragment<Any>()
{
	var m_game: GameSummary by Syringe()

	override fun getLayoutResourceId() = R.layout.links_fragment

	override fun createModel(): String = ""

	override fun loadData()
	{
		val gameDetailCreator = GameDetailCreator(m_game.gameDataDirectory, false)
		gameDetailCreator.getGameCenter({ data -> onDataLoaded(data) })
	}

	private fun onDataLoaded(data: GameCenter?)
	{

		m_adapter?.clear()

		val boxScore = LinkItem("Box Score", null, R.drawable.ic_view_module_white_24px)
		boxScore.setClickListener(BoxScoreClickListener())
		m_adapter?.add(boxScore)

		val cid = data?.recaps?.mlb?.url?.cid
		if (cid != null)
		{
			val recap = LinkItem("Recap", "mlb.com", R.drawable.ic_newspaper)
			recap.setClickListener(RecapClickListener(cid))
			m_adapter?.add(recap)
		}
	}

	fun openCustomTab(context: Context, url: String)
	{
		val builder = CustomTabsIntent.Builder()
		val customTabsIntent = builder.build()
		customTabsIntent.launchUrl(context, Uri.parse(url))
	}

	companion object
	{
		fun newInstance(game: GameSummary): LinksFragment
		{
			return LinksFragment().apply {
				m_game = game
			}
		}
	}

	inner class BoxScoreClickListener : ItemClickListener
	{
		override fun invoke(view: View, position: Int)
		{
			val fmt = DateTimeFormat.forPattern("yyyyMMdd")
			val dateString = fmt.print(m_game.dateObj())
			val url = "https://baseball.theater/game/" + dateString + "/" + m_game.gamePk + "?app=true"
			openCustomTab(view.context, url)
		}
	}

	inner class RecapClickListener(val m_cid: Long) : ItemClickListener
	{
		override fun invoke(view: View, position: Int)
		{
			val url = "http://m.mlb.com/news/article/$m_cid"
			openCustomTab(view.context, url)
		}
	}
}
package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.AtBat
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.*
import com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.highlights.HighlightItem
import com.jakelauer.baseballtheater.utils.Utils
import libs.ButterKnife.bindView
import net.cachapa.expandablelayout.ExpandableLayout
import java.lang.Float.parseFloat


/**
 * Created by Jake on 2/12/2018.
 */
class BatterItem(data: BatterItem.Data, private val m_isSpringTraining: Boolean, private val m_activity: BaseActivity) : AdapterChildItem<BatterItem.Data, BatterItem.ViewHolder>(data)
{
	private var m_isExpanded = false
	private var m_resultClickListener: ItemClickListener? = null
	private var m_viewHolder: ViewHolder? = null
	private var m_relatedHighlight = getRelatedHighlight()

	override fun getLayoutResId() = R.layout.play_by_play_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		m_viewHolder = viewHolder

		viewHolder.m_resultText.text = m_data.m_play.des

		viewHolder.m_playIcon.alpha = if (m_relatedHighlight != null) 1f else 0.2f

		setListeners(viewHolder)

		toggleExpanded(viewHolder.itemView.context, forceExpanded = false)
	}

	private fun addPitchLocations(context: Context, viewHolder: ViewHolder)
	{
		val regularSeason = object : ISizes
		{
			override val maxX = 300f
			override val maxY = 165f
			override val yOffset = -0.55f
			override val xOffset = -0.10f
		}

		val springTraining = object : ISizes
		{
			override val maxX = 300f
			override val maxY = 300f
			override val yOffset = 0f
			override val xOffset = -0.13f
		}

		val container = viewHolder.m_strikezonePitchesContainer
		container.removeAllViews()

		m_data.m_play.pitches.forEachIndexed { i, pitch ->
			val pitchX = parseFloat(pitch.x)
			val pitchY = parseFloat(pitch.y)

			val sizes = if (m_isSpringTraining) springTraining else regularSeason

			val leftPct = 1 - (pitchX / sizes.maxX) + sizes.xOffset
			val topPct = (pitchY / sizes.maxY) + sizes.yOffset

			val view = TextView(context)

			val params = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
			params.topToTop = R.id.PITCHES_strikezone_pitches_container
			params.bottomToBottom = R.id.PITCHES_strikezone_pitches_container
			params.startToStart = R.id.PITCHES_strikezone_pitches_container
			params.endToEnd = R.id.PITCHES_strikezone_pitches_container
			params.horizontalBias = leftPct
			params.verticalBias = topPct

			view.text = (i + 1).toString()
			view.gravity = Gravity.CENTER
			view.background = context.getDrawable(R.drawable.pitch_circle)
			view.setTextColor(Color.WHITE)

			val bg = Utils.getColorFromPitchResult(pitch, context)
			if (bg != null)
			{
				view.background.setColorFilter(bg, PorterDuff.Mode.SRC_ATOP)
			}

			container.addView(view, -1, params)
		}
	}

	private fun getSvIdsForPlay(play: AtBat): List<String>
	{
		if (play.pitches != null && play.pitches.any())
		{
			return play.pitches.map { it.sv_id }
		}

		return ArrayList()
	}

	private fun getRelatedHighlight(): Highlight?
	{
		val guid = m_data.m_play.play_guid
		val svIds = this.getSvIdsForPlay(m_data.m_play)
		val hc = m_data.m_highlights
		var foundHighlight: Highlight? = null

		if (hc?.highlights != null)
		{
			hc.highlights.forEach { highlight ->
				var found = false
				if (highlight.keywords != null)
				{
					highlight.keywords.forEach({ keyword ->
						found = found || (keyword.type == "sv_id" && (keyword.value == guid || svIds.indexOf(keyword.value) > -1))
					})
				}

				if (found)
				{
					foundHighlight = highlight
				}
			}
		}

		return foundHighlight
	}

	fun setResultClickListener(l: ItemClickListener)
	{
		m_resultClickListener = l
	}

	private fun addPitchList(context: Context, viewHolder: ViewHolder)
	{
		val pitchList = ArrayList<PitchListItem>()
		m_data.m_play.pitches.forEachIndexed { i, pitch ->
			pitchList.add(PitchListItem(PitchListItem.Data(pitch, i)))
		}

		val pitchListAdapter = ComplexArrayAdapter(context, pitchList)
		viewHolder.m_pitchListContainer.adapter = pitchListAdapter
	}

	private fun setListeners(viewHolder: ViewHolder)
	{
		viewHolder.m_playIcon.setOnClickListener({ _ -> })

		viewHolder.m_resultText.setOnClickListener({ view ->
			toggleExpanded(view.context)
			m_resultClickListener?.invoke(view, 0)
		})

		val rh = m_relatedHighlight
		if (rh != null)
		{
			val hd = HighlightItem.HighlightData(rh)
			viewHolder.m_playIcon.setOnClickListener(HighlightItem.HighlightClickListener(m_activity, hd, hd.video_m))
		}
	}

	fun toggleExpanded(context: Context, forceExpanded: Boolean? = null)
	{
		m_viewHolder?.let { viewHolder ->
			var willExpand = !m_isExpanded
			if (forceExpanded != null)
			{
				willExpand = forceExpanded
			}

			viewHolder.m_pitchesContainer.clipChildren = false

			TransitionManager.beginDelayedTransition(viewHolder.m_pitchesContainer)

			if (willExpand)
			{
				viewHolder.m_expandable.expand()

				addPitchList(context, viewHolder)

				addPitchLocations(context, viewHolder)
			}
			else
			{
				viewHolder.m_expandable.collapse()
			}

			m_isExpanded = willExpand
		}
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_playIcon: AppCompatImageView by bindView(R.id.PLAY_BY_PLAY_ITEM_icon)
		val m_expandable: ExpandableLayout by bindView(R.id.PLAY_BY_PLAY_expandable_layout)
		val m_resultText: TextView by bindView(R.id.PLAY_BY_PLAY_ITEM_result)
		val m_pitchesContainer: LinearLayout by bindView(R.id.PLAY_BY_PLAY_pitches_container)
		val m_pitchListContainer: ListView by bindView(R.id.PITCHES_pitch_list)
		val m_strikezonePitchesContainer: ConstraintLayout by bindView(R.id.PITCHES_strikezone_pitches_container)
		val m_strikezoneContainer: ConstraintLayout by bindView(R.id.PITCHES_strikezone_container)
	}

	interface ISizes
	{
		val maxY: Float
		val maxX: Float
		val yOffset: Float
		val xOffset: Float
	}

	class Data(val m_play: AtBat, val m_highlights: HighlightsCollection?)
}
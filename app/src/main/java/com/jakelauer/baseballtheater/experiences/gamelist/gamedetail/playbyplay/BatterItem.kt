package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.AtBat
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ComplexArrayAdapter
import com.jakelauer.baseballtheater.base.ItemClickListener
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.jakelauer.baseballtheater.utils.Utils
import libs.ButterKnife.bindView
import java.lang.Float.parseFloat


/**
 * Created by Jake on 2/12/2018.
 */
class BatterItem(data: AtBat) : AdapterChildItem<AtBat, BatterItem.ViewHolder>(data)
{
	private var m_isExpanded = false
	private var m_resultClickListener: ItemClickListener? = null
	private var m_viewHolder: ViewHolder? = null

	override fun getLayoutResId() = R.layout.play_by_play_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		m_viewHolder = viewHolder

		viewHolder.m_resultText.text = m_data.des

		setListeners(viewHolder)

		toggleExpanded(viewHolder.itemView.context, forceExpanded = false)
	}

	private fun addPitchLocations(context: Context, viewHolder: ViewHolder)
	{
		val minX = 15
		val maxY = 250
		val minY = 100
		val maxX = 250

		val container = viewHolder.m_strikezonePitchesContainer
		val pitchWidth = Utils.dpToPx(20, context)
		val pitchRadius = pitchWidth / 2

		container.removeAllViews()

		m_data.pitches.forEachIndexed { i, pitch ->
			val pitchX = parseFloat(pitch.x)
			val pitchY = parseFloat(pitch.y)

			val leftPct = ((maxX - pitchX - minX)) / (maxX - minX)
			val topPct = (pitchY - minY) / (maxY - minY)
			val leftAmt = (leftPct * container.width).toInt() - pitchRadius
			val topAmt = (topPct * container.height.toFloat()).toInt() - pitchRadius

			val view = TextView(context)

			val params = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
			params.leftMargin = leftAmt
			params.topMargin = topAmt

			view.text = (i + 1).toString()
			view.gravity = Gravity.CENTER
			view.background = context.getDrawable(R.drawable.pitch_circle)
			view.setTextColor(Color.WHITE)

			val bg = Utils.getColorFromPitchResult(pitch, context)
			if (bg != null)
			{
				view.background.setColorFilter(bg, PorterDuff.Mode.SRC_ATOP);
			}

			container.addView(view, params)
		}
	}

	fun setResultClickListener(l: ItemClickListener)
	{
		m_resultClickListener = l
	}

	private fun addPitchList(context: Context, viewHolder: ViewHolder)
	{
		val pitchList = ArrayList<PitchListItem>()
		m_data.pitches.forEachIndexed { i, pitch ->
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

			val newLp = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
			newLp.topToBottom = R.id.PLAY_BY_PLAY_ITEM_result

			if (willExpand)
			{
				addPitchList(context, viewHolder)

				newLp.bottomToBottom = -1

				addPitchLocations(context, viewHolder)
			}
			else
			{
				newLp.bottomToBottom = R.id.PLAY_BY_PLAY_ITEM_result
			}

			viewHolder.m_pitchesContainer.layoutParams = newLp

			m_isExpanded = willExpand
		}
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_playIcon: AppCompatImageView by bindView(R.id.PLAY_BY_PLAY_ITEM_icon)
		val m_resultText: TextView by bindView(R.id.PLAY_BY_PLAY_ITEM_result)
		val m_pitchesContainer: LinearLayout by bindView(R.id.PLAY_BY_PLAY_pitches_container)
		val m_pitchListContainer: ListView by bindView(R.id.PITCHES_pitch_list)
		val m_strikezonePitchesContainer: RelativeLayout by bindView(R.id.PITCHES_strikezone_pitches_container)
		val m_strikezoneContainer: ConstraintLayout by bindView(R.id.PITCHES_strikezone_container)
	}
}
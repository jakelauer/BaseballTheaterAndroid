package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.AtBat
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ComplexArrayAdapter
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView

/**
 * Created by Jake on 2/12/2018.
 */
class BatterItem(data: AtBat) : AdapterChildItem<AtBat, BatterItem.ViewHolder>(data)
{
	private var m_isExpanded = false

	override fun getLayoutResId() = R.layout.play_by_play_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_resultText.text = m_data.des

		setListeners(viewHolder)

		toggleExpanded(viewHolder, viewHolder.itemView.context, forceExpanded = false)
	}

	private fun addPitchLocations()
	{
		for (pitch in m_data.pitches)
		{

		}
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

		viewHolder.m_resultText.setOnClickListener({ view -> toggleExpanded(viewHolder, view.context) })
	}

	private fun toggleExpanded(viewHolder: ViewHolder, context: Context, forceExpanded: Boolean? = null)
	{
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
			addPitchLocations()

			newLp.bottomToBottom = -1
		}
		else
		{
			newLp.bottomToBottom = R.id.PLAY_BY_PLAY_ITEM_result
		}

		viewHolder.m_pitchesContainer.layoutParams = newLp

		m_isExpanded = willExpand
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_playIcon: AppCompatImageView by bindView(R.id.PLAY_BY_PLAY_ITEM_icon)
		val m_resultText: TextView by bindView(R.id.PLAY_BY_PLAY_ITEM_result)
		val m_pitchesContainer: LinearLayout by bindView(R.id.PLAY_BY_PLAY_pitches_container)
		val m_pitchListContainer: ListView by bindView(R.id.PITCHES_pitch_list)
		val m_strikezoneContainer: ConstraintLayout by bindView(R.id.PITCHES_strikezone_container)
	}
}
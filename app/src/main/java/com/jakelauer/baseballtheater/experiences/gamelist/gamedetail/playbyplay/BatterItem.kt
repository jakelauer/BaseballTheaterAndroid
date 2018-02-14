package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.AtBat
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.Pitch
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ComplexAdapter
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView

/**
 * Created by Jake on 2/12/2018.
 */
class BatterItem(data: AtBat) : AdapterChildItem<AtBat, BatterItem.ViewHolder>(data)
{
	private var m_isExpanded = false
	private lateinit var m_pitchListAdapter: ArrayAdapter<PitchListItem>

	override fun getLayoutResId() = R.layout.play_by_play_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		m_pitchListAdapter = ArrayAdapter(context, R.layout.pitch_list_item)
		viewHolder.m_pitchListContainer.adapter = m_pitchListAdapter

		viewHolder.m_resultText.text = m_data.des

		setListeners(viewHolder)

		addPitchLocations(context)
		addPitchList()
	}

	fun addPitchLocations(context: Context)
	{
		for (pitch in m_data.pitches)
		{

		}
	}

	fun addPitchList()
	{
		m_data.pitches.forEachIndexed {
			i, pitch -> m_pitchListAdapter.add(PitchListItem(PitchListItem.Data(pitch, i)))
		}
	}

	fun setListeners(viewHolder: ViewHolder)
	{
		viewHolder.m_playIcon.setOnClickListener({ _ -> })

		viewHolder.m_resultText.setOnClickListener({ _ -> onResultTextClick(viewHolder) })
	}

	fun onResultTextClick(viewHolder: ViewHolder)
	{
		viewHolder.m_pitchesContainer.clipChildren = false

		TransitionManager.beginDelayedTransition(viewHolder.m_pitchesContainer)

		val newLp = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
		newLp.topToBottom = R.id.PLAY_BY_PLAY_ITEM_result

		if (!m_isExpanded)
		{
			newLp.bottomToBottom = -1
		}
		else
		{
			newLp.bottomToBottom = R.id.PLAY_BY_PLAY_ITEM_result
		}

		viewHolder.m_pitchesContainer.layoutParams = newLp

		m_isExpanded = !m_isExpanded
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
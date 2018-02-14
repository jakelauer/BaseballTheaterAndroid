package com.jakelauer.baseballtheater.experiences.gamelist.gamedetail.playbyplay

import android.content.Context
import android.view.View
import android.widget.TextView
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings.Pitch
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView

/**
 * Created by Jake on 2/13/2018.
 */
class PitchListItem(data: PitchListItem.Data) : AdapterChildItem<PitchListItem.Data, PitchListItem.ViewHolder>(data)
{
	override fun getLayoutResId() = R.layout.pitch_list_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_pitchIndex.text = (m_data.m_index + 1).toString()

		viewHolder.m_pitchDesc.text = m_data.m_pitch.des

		val startSpeed = m_data.m_pitch.start_speed ?: ""
		val pitchTypeDetail =m_data.m_pitch.pitchTypeDetail ?: ""

		viewHolder.m_pitchInfo.text = context.getString(R.string.GAME_DETAIL_pitch_detail,
				startSpeed,
				pitchTypeDetail)
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		val m_pitchIndex: TextView by bindView(R.id.PITCH_LIST_ITEM_pitch_index)
		val m_pitchInfo: TextView by bindView(R.id.PITCH_LIST_ITEM_pitch_info)
		val m_pitchDesc: TextView by bindView(R.id.PITCH_LIST_ITEM_pitch_description)
	}

	class Data(val m_pitch: Pitch, val m_index: Int)
}
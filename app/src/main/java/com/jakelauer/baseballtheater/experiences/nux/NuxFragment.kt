package com.jakelauer.baseballtheater.experiences.nux

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import com.jakelauer.baseballtheater.MainActivity
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.utils.PrefUtils
import libs.ButterKnife.bindView

/**
 * Created by Jake on 11/9/2017.
 */

class NuxFragment : BaseFragment<String>
{
	constructor() : super()

	var m_listView: ListView by bindView(R.id.NUX_fav_team_spinner)
	var m_button: Button by bindView(R.id.NUX_skip)

	lateinit var m_teamKeys: Array<String>

	override fun getLayoutResourceId() = R.layout.nux_fragment

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		m_teamKeys = context.resources.getStringArray(R.array.teams_list_values)
	}

	override fun createModel(): String
	{
		return ""
	}

	override fun loadData()
	{
	}

	override fun onBindView()
	{
		val favTeam = PrefUtils.getString(context, PrefUtils.BEHAVIOR_FAVORITE_TEAM)

		val selectedIndex = m_teamKeys.indexOf(favTeam)

		if (selectedIndex > -1)
		{
			m_listView.clearFocus()
			m_listView.post({
				m_listView.setSelection(selectedIndex)
				m_listView.setItemChecked(selectedIndex, true)
			})
		}

		m_listView.onItemClickListener = Listener()

		m_button.setOnClickListener({
			val intent = Intent(context, MainActivity::class.java)
			startActivity(intent)
		})
	}

	inner class Listener : AdapterView.OnItemClickListener
	{
		override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
		{
			PrefUtils.set(context, PrefUtils.BEHAVIOR_FAVORITE_TEAM, m_teamKeys[position])
			m_button.isEnabled = true
			m_button.alpha = 1f
		}
	}
}
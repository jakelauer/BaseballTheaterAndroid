package com.jakelauer.baseballtheater.experiences.gamelist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.syringe.syringe
import com.jakelauer.baseballtheater.experiences.settings.SettingsActivity
import libs.ButterKnife.bindView
import org.joda.time.DateTime


/**
 * Created by Jake on 10/20/2017.
 */

class GameListPagerFragment : BaseFragment<Any>
{
	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(initialFragmentDate: DateTime) : super(initialFragmentDate)

	var m_initialFragmentDate: DateTime by syringe()
	lateinit var m_startingDate: DateTime
	lateinit var m_currentDate: DateTime

	val m_gamePager: ViewPager by bindView(R.id.game_pager)

	lateinit var m_gamePagerAdapter: GameListPagerAdapter

	override fun getLayoutResourceId(): Int = R.layout.game_list_pager_fragment

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		m_startingDate = m_initialFragmentDate
		m_currentDate = m_initialFragmentDate

		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater)
	{
		menuInflater.inflate(R.menu.game_list_options, menu)
		super.onCreateOptionsMenu(menu, menuInflater)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean
	{
		var handled = super.onOptionsItemSelected(item)

		when (item?.itemId)
		{
			R.id.game_list_settings ->
			{
				val intent = Intent(context, SettingsActivity::class.java)
				context.startActivity(intent)
				handled = true
			}
			R.id.game_list_date_picker ->
			{
				val newFragment = DatePickerFragment()
				newFragment.show(childFragmentManager, "datePicker")
			}
		}
		return handled
	}

	override fun onBindView()
	{
		m_gamePagerAdapter = GameListPagerAdapter(fragmentManager)

		refreshAdapter()
	}

	override fun createModel(): Any
	{
		return ""
	}

	override fun loadData()
	{

	}

	fun setToolbarDate(date: DateTime)
	{
		val dateString = date.toString("MMM d, yyyy")
		activity.title = dateString
	}

	fun refreshWithDate(date: DateTime)
	{
		m_startingDate = date
		refreshAdapter()
	}

	fun refreshAdapter()
	{
		m_gamePager.adapter = null
		m_gamePager.adapter = m_gamePagerAdapter
		m_gamePager.currentItem = m_gamePagerAdapter.count / 2
		m_gamePager.addOnPageChangeListener(GameListPagerChangeListener())

		setDate(m_startingDate)
	}

	fun setDate(date: DateTime)
	{
		setToolbarDate(date)
		m_currentDate = date
		(activity as BaseActivity).setPref("date", date.toString())
	}

	fun getCurrentDate(): DateTime
	{
		return m_currentDate
	}

	inner class GameListPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm)
	{
		private val m_dayCount = 400
		private val m_startingPosition = m_dayCount / 2

		override fun getItem(position: Int): Fragment
		{
			val newDate = getDateFromPosition(position)

			return GameListFragment(newDate)
		}

		fun getDateFromPosition(position: Int): DateTime
		{
			val diff = position - m_startingPosition

			var newDate = DateTime(m_startingDate)
			newDate = newDate.plusDays(diff)

			return newDate
		}

		override fun getCount(): Int
		{
			return m_dayCount
		}
	}

	inner class GameListPagerChangeListener : ViewPager.OnPageChangeListener
	{
		var m_currentPage = 0
		var m_state = 0

		override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
		{
			if (m_state != ViewPager.SCROLL_STATE_SETTLING)
			{
				val newPosition: Int
				if (position == m_currentPage)
				{
					newPosition = if (positionOffset > 0.5) position + 1 else position
				}
				else
				{
					newPosition = if (positionOffset < 0.5) position else position + 1
				}

				val diff = newPosition - m_currentPage

				if (diff != 0)
				{
					val setToDate = m_gamePagerAdapter.getDateFromPosition(newPosition)
					setDate(setToDate)
					Log.d("setPageScrolled", "1")
				}

				m_currentPage = newPosition
			}
		}

		override fun onPageSelected(position: Int)
		{
			m_currentPage = position
			val setToDate = m_gamePagerAdapter.getDateFromPosition(position)
			setDate(setToDate)
			Log.d("setPageSelected", "1")
		}

		override fun onPageScrollStateChanged(state: Int)
		{
			m_state = state
		}
	}
}
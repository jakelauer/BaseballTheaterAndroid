package com.jakelauer.baseballtheater.experiences.gamelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.widget.TextView
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseActivity
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.utils.Inject
import libs.bindView
import org.joda.time.DateTime


/**
 * Created by Jake on 10/20/2017.
 */

class GameListPagerFragment : BaseFragment<Any>
{
	constructor() : super()

	@SuppressLint("ValidFragment")
	constructor(startingDate: DateTime) : super(startingDate)

	var m_startingDate: DateTime by Inject<DateTime>()

	val m_gamePager: ViewPager by bindView(R.id.game_pager)
	val m_castButton: MediaRouteButton by bindView(R.id.media_route_button)
	var m_toolbarDate: TextView by bindView(R.id.game_list_toolbar_date)

	lateinit var m_gamePagerAdapter: GameListPagerAdapter
	lateinit var m_castContext: CastContext

	override fun getLayoutResourceId(): Int = R.layout.game_list_pager_fragment

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
	}

	override fun onBindView()
	{
		m_gamePagerAdapter = GameListPagerAdapter(fragmentManager)
		m_gamePager.adapter = m_gamePagerAdapter
		m_gamePagerAdapter.setDate(m_startingDate)
		m_gamePager.currentItem = m_gamePagerAdapter.count / 2
		m_gamePager.addOnPageChangeListener(GameListPagerChangeListener())

		setToolbarDate(m_startingDate)

		CastButtonFactory.setUpMediaRouteButton(context, m_castButton)
		m_castContext = CastContext.getSharedInstance(context);
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
		m_toolbarDate.text = dateString
	}

	inner class GameListPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm)
	{
		private val m_dayCount = 400
		private val m_startingPosition = m_dayCount / 2
		private var m_forceReplaceFlag: Boolean? = false

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

		fun setDate(date: DateTime)
		{
			m_forceReplaceFlag = true
			setToolbarDate(date)
			(activity as BaseActivity).setPref("date", date.toString())
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
					m_gamePagerAdapter.setDate(setToDate)
					Log.d("setPageScrolled", "1")
				}

				m_currentPage = newPosition
			}
		}

		override fun onPageSelected(position: Int)
		{
			m_currentPage = position
			val setToDate = m_gamePagerAdapter.getDateFromPosition(position)
			m_gamePagerAdapter.setDate(setToDate)
			Log.d("setPageSelected", "1")
		}

		override fun onPageScrollStateChanged(state: Int)
		{
			m_state = state
		}
	}
}
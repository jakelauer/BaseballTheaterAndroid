package com.jakelauer.baseballtheater.experiences.gamelist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.f2prateek.dart.InjectExtra
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import libs.bindView
import org.joda.time.DateTime


/**
 * Created by Jake on 10/20/2017.
 */

class GameListPagerFragment : BaseFragment<Any>()
{
	companion object
	{
		const val ARG_DATE = "ARG_DATE"

		@JvmStatic
		fun newInstance(date: DateTime): GameListPagerFragment
		{
			val args = Bundle()
			args.putSerializable(ARG_DATE, date)
			val fragment = GameListPagerFragment()
			fragment.arguments = args
			return fragment
		}
	}

	@InjectExtra(ARG_DATE)
	lateinit var m_startingDate: DateTime

	val m_gamePager: ViewPager by bindView(R.id.game_pager)
	lateinit var m_gamePagerAdapter: GameListPagerAdapter

	var m_toolbarDate: TextView by bindView(R.id.game_list_toolbar_date)

	override fun getLayoutResourceId(): Int = R.layout.game_list_pager_fragment

	override fun onBindView()
	{
		m_gamePagerAdapter = GameListPagerAdapter(fragmentManager)
		m_gamePager.adapter = m_gamePagerAdapter
		m_gamePagerAdapter.setDate(m_startingDate)
		m_gamePager.currentItem = m_gamePagerAdapter.count / 2
		m_gamePager.addOnPageChangeListener(GameListPagerChangeListener())

		setToolbarDate(m_startingDate)
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
		m_toolbarDate.setText(dateString)
	}

	inner class GameListPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm)
	{
		private val m_dayCount = 400
		private val m_startingPosition = m_dayCount / 2
		private var m_forceReplaceFlag: Boolean? = false

		override fun getItem(position: Int): Fragment
		{
			val newDate = getDateFromPosition(position)

			val newFragment = GameListFragment.newInstance(newDate)

			return newFragment
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
		}

		override fun getCount(): Int
		{
			return m_dayCount
		}
	}

	inner class GameListPagerChangeListener : ViewPager.OnPageChangeListener
	{
		var m_currentPage = 0;

		override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
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
			}

			m_currentPage = newPosition
		}

		override fun onPageSelected(position: Int)
		{
			m_currentPage = position
			val setToDate = m_gamePagerAdapter.getDateFromPosition(position)
			m_gamePagerAdapter.setDate(setToDate)
		}

		override fun onPageScrollStateChanged(state: Int)
		{
		}
	}
}
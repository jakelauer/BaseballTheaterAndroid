package com.jakelauer.baseballtheater.gamelist

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.f2prateek.dart.InjectExtra
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import libs.bindView
import org.joda.time.DateTime
import java.util.*
import org.joda.time.format.DateTimeFormat


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
	lateinit var m_date: DateTime

	val m_gamePager: ViewPager by bindView(R.id.game_pager)

	override fun getLayoutResourceId(): Int = R.layout.game_list_pager_fragment

	override fun onBindView()
	{
		m_gamePager.adapter = GameListPagerAdapter(fragmentManager, context.applicationContext)
		(m_gamePager.adapter as GameListPagerAdapter).setDate(getStartingDate())
	}

	override fun createModel(): Any
	{
		return ""
	}

	override fun loadData()
	{

	}

	fun getStartingDate(): DateTime
	{
		val today = DateTime()
		val finalDay2017: DateTime
		val openingDay2018: DateTime
		val final2017String = "20171102"
		val openingDay2018String = "20180303"

		val fmt = DateTimeFormat.forPattern("yyyyMMdd")
		finalDay2017 = DateTime.parse(final2017String, fmt)
		openingDay2018 = DateTime.parse(openingDay2018String, fmt)

		var returnTime = today
		if (today.isBefore(finalDay2017))
		{
			returnTime = finalDay2017
		}
		else if (today.year > 2017)
		{
			returnTime = openingDay2018
		}

		return returnTime
	}

	class GameListPagerAdapter(fm: FragmentManager, context: Context) : FragmentStatePagerAdapter(fm)
	{
		protected var m_context: Context = context
		private var m_startingDate: DateTime = DateTime.now()
		private val cal = Calendar.getInstance()

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
			m_startingDate = date
			m_forceReplaceFlag = true
		}

		override fun getCount(): Int
		{
			return m_dayCount
		}
	}
}
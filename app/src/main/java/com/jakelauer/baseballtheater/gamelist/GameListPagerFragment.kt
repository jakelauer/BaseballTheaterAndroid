package com.jakelauer.baseballtheater.gamelist

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import butterknife.BindView
import com.f2prateek.dart.InjectExtra
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.ListFragment
import com.jakelauer.baseballtheater.base.ItemViewHolder
import kotlinx.android.synthetic.main.game_list_pager_fragment.*
import org.joda.time.DateTime
import java.util.*


/**
 * Created by Jake on 10/20/2017.
 */

class GameListPagerFragment : BaseFragment<Void?>()
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
    var m_date: DateTime? = null

    @BindView(R.id.game_pager)
    @JvmField
    var m_gamePager: ViewPager? = null

    override fun getLayoutResourceId(): Int = R.layout.game_list_pager_fragment

    override fun onBindView()
    {
        m_gamePager?.adapter = GameListPagerAdapter(fragmentManager, context.applicationContext)
    }

    override fun createModel(): Void?
    {
        return null
    }

    override fun loadData()
    {

    }

    class GameListPagerAdapter(fm: FragmentManager, context: Context) : FragmentStatePagerAdapter(fm)
    {
        protected var m_context: Context = context
        private var m_startingDate: DateTime? = null
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
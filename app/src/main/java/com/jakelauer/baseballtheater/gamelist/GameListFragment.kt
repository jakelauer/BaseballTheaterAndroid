package com.jakelauer.baseballtheater.gamelist

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.View
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.BaseFragment
import com.jakelauer.baseballtheater.base.ItemViewHolder
import org.joda.time.DateTime

/**
 * Created by Jake on 10/20/2017.
 */

class GameListFragment : BaseFragment()
{
    override fun getLayoutResourceId(): Int = R.layout.game_list_fragment

    companion object
    {
        const val ARG_DATE = "ARG_DATE"
        fun newInstance(date: DateTime): Fragment
        {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = GameListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onBindView()
    {
    }

    override fun getViewHolder(view: View): ItemViewHolder
    {
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : ItemViewHolder(view)
    {

    }

}
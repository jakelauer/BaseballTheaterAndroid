package com.jakelauer.baseballtheater.GameList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jake on 1/22/2017.
 */

public class GameListStatePagerAdapter extends FragmentStatePagerAdapter {
	protected Context mContext;
	private DateTime mStartingDate;
	private Calendar cal = Calendar.getInstance();

	private int mDayCount = 400;
	private int mStartingPosition = mDayCount / 2;
	private Boolean mForceReplaceFlag = false;

	public GameListStatePagerAdapter(FragmentManager fm, Context context) {
		super(fm);

		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		DateTime newDate = getDateFromPosition(position);

		Fragment newFragment = GameListActivityFragment.newInstance(newDate, mForceReplaceFlag);

		mForceReplaceFlag = false;

		return newFragment;
	}

	public DateTime getDateFromPosition(int position){
		int diff = position - mStartingPosition;

		DateTime newDate = new DateTime(mStartingDate);
		newDate = newDate.plusDays(diff);

		return newDate;
	}

	public void setDate(DateTime date){
		mStartingDate = date;
		mForceReplaceFlag = true;
	}

	@Override
	public int getCount() {
		return mDayCount;
	}
}

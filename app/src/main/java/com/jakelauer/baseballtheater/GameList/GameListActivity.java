package com.jakelauer.baseballtheater.GameList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import icepick.Icepick;

public class GameListActivity extends AppCompatActivity implements ProgressActivity {

	private String openingDay2016String = "20160403";
	private String openingDay2017String = "20170402";
	private Date openingDay2016;
	private Date openingDay2017;
	private Date today = new Date();
	private Calendar cal = Calendar.getInstance();
	private ProgressDialog progressDialog;

	private GameListStatePagerAdapter mGameListStatePagerAdapter;
	private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);

		setContentView(R.layout.activity_game_list);

		if(getDate() == null) {
			try {
				openingDay2016 = new SimpleDateFormat("yyyyMMdd").parse(openingDay2016String);
				openingDay2017 = new SimpleDateFormat("yyyyMMdd").parse(openingDay2017String);

				setDate(today.after(openingDay2017) ? today : openingDay2016);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}


		if(savedInstanceState == null){
			refreshCurrentView();
		}

		setTitle();
       	createFab();

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Loading");
		progressDialog.setCancelable(false);
		progressDialog.show();
    }

	protected Date getDate(){
		BaseballTheater application = (BaseballTheater) getApplication();
		return application.getGameListDate();
	}

	private void setDate(Date newdate){
		BaseballTheater application = (BaseballTheater) getApplication();
		application.setGameListDate(newdate);
		cal.setTime(newdate);
	}

	private void setTitle(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Date date = getDate();
		String titleDate = new SimpleDateFormat("MMMM d, yyyy").format(date);
		getSupportActionBar().setTitle(getTitle() + " - " + titleDate);
	}

	private void refreshCurrentView() {

		if(mGameListStatePagerAdapter == null) {
			mGameListStatePagerAdapter = new GameListStatePagerAdapter(getSupportFragmentManager(), getApplicationContext());
			mViewPager = (ViewPager) findViewById(R.id.game_pager);

			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				}

				@Override
				public void onPageSelected(int position) {
					setDate(mGameListStatePagerAdapter.getDateFromPosition(position));
				}

				@Override
				public void onPageScrollStateChanged(int state) {
				}
			});
		}

		mViewPager.setAdapter(null);
		mViewPager.setAdapter(mGameListStatePagerAdapter);
		mGameListStatePagerAdapter.setDate(getDate());
		mViewPager.setCurrentItem((int) Math.floor(mGameListStatePagerAdapter.getCount() / 2));

		setTitle();
	}

	private void createFab(){
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatePickerFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
	}

	@Override
	public void onProgressUpdate(double progress) {
	}

	@Override
	public void onProgressFinished(Object objectInstance) {
		progressDialog.dismiss();
		setTitle();
	}

	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			GameListActivity gameListActivity = (GameListActivity) getActivity();

			// Use the current date as the default date in the picker
			int year = gameListActivity.cal.get(Calendar.YEAR);
			int month = gameListActivity.cal.get(Calendar.MONTH);
			int day = gameListActivity.cal.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			String dateString = year + "/" + (month + 1) + "/" + day;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			try {
				GameListActivity gameListActivity = (GameListActivity) getActivity();
				gameListActivity.setDate(formatter.parse(dateString));
				gameListActivity.refreshCurrentView();
				gameListActivity.progressDialog.show();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
	}
}

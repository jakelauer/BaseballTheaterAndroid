package com.jakelauer.baseballtheater.GameList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import icepick.Icepick;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

public class GameListActivity extends AppCompatActivity implements ProgressActivity {

	private String openingDay2016String = "20160404";
	private String openingDay2017String = "20170402";
	private Date openingDay2016;
	private Date openingDay2017;
	private Date today = new Date();
	private Calendar cal = Calendar.getInstance();
	private ProgressDialog progressDialog;

	private SharedPreferences mPrefs;
	private GameListStatePagerAdapter mGameListStatePagerAdapter;
	private ViewPager mViewPager;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ImageView mDrawerImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.activity_game_list);

		initializeDrawer();

		if (getDate() == null) {
			try {
				openingDay2016 = new SimpleDateFormat("yyyyMMdd").parse(openingDay2016String);
				openingDay2017 = new SimpleDateFormat("yyyyMMdd").parse(openingDay2017String);

				setDate(today.after(openingDay2017) ? today : openingDay2016);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}


		if (savedInstanceState == null) {
			refreshCurrentView();
		}

		setTitle();
		createFab();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Loading");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	protected Date getDate() {
		BaseballTheater application = (BaseballTheater) getApplication();
		return application.getGameListDate();
	}

	private void setDate(Date newdate) {
		BaseballTheater application = (BaseballTheater) getApplication();
		application.setGameListDate(newdate);
		cal.setTime(newdate);
	}

	private void setTitle() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Date date = getDate();
		String titleDate = new SimpleDateFormat("MMMM d, yyyy").format(date);
		getSupportActionBar().setTitle(getTitle() + " - " + titleDate);
	}

	private void refreshCurrentView() {

		if (mGameListStatePagerAdapter == null) {
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

	private void initializeDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list);
		mDrawerImage = (ImageView) findViewById(R.id.left_drawer_image);

		final String favTeamCode = mPrefs.getString("behavior_favorite_team", "none");
		String imageName = "team_splash_" + favTeamCode;
		int teamLogoResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

		Picasso.with(context)
				.load(teamLogoResourceId)
				.placeholder(R.color.colorPlaceholder)
				.into(mDrawerImage);

		List<DrawerRowItem> listItems = new ArrayList<>(Arrays.asList(
				new DrawerRowItem("Settings", R.drawable.ic_settings),
				new DrawerRowItem("About", R.drawable.ic_info),
				new DrawerRowItem("Feedback", R.drawable.ic_feedback),
				new DrawerRowItem("Buy Me a Beer", R.drawable.ic_beer)
		));

		mDrawerList.setAdapter(new DrawerArrayAdapter(this, R.layout.drawer_list_item, listItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.addDrawerListener(mDrawerToggle);
		mDrawerToggle.setDrawerIndicatorEnabled(true);
	}

	private void createFab() {
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	protected void onResume(){
		super.onResume();

		if(BaseballTheater.getSettingsChanged()) {
			Intent intent = getIntent();
			finish();
			startActivity(intent);

			BaseballTheater.setSettingsChanged(false);
		}
	}

}
package com.jakelauer.baseballtheater.GameList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;
import com.jakelauer.baseballtheater.Settings.SettingsActivity;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import icepick.Icepick;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static dk.nodes.okhttputils.error.HttpErrorManager.context;

public class GameListActivity extends AppCompatActivity implements ProgressActivity
{

	private DateTime today = new DateTime();

	private SharedPreferences mPrefs;
	private GameListStatePagerAdapter mGameListStatePagerAdapter;
	public int mLastValidNoGamesVisibilityState = GONE;

	private ViewPager mViewPager;
	private DrawerLayout mDrawerLayout;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private LinearLayout mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ImageView mDrawerImage;
	private LinearLayout mPatreon;
	private ProgressBar mPatreonProgress;
	private TextView mNoGamesFoundView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.activity_game_list);
		mNoGamesFoundView = (TextView) this.findViewById(R.id.no_games_found);

		initializeDrawer();
		initializeRefreshView();
		setupInitialDate();

		refreshCurrentView();

		setTitle();
		createFab();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void setupInitialDate()
	{
		if (getDate() == null)
		{
			DateTime openingDay2016;
			DateTime openingDay2017;
			String openingDay2016String = "20160404";
			String openingDay2017String = "20170222";

			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
			openingDay2016 = DateTime.parse(openingDay2016String, fmt);
			openingDay2017 = DateTime.parse(openingDay2017String, fmt);

			setDate(today.isAfter(openingDay2017) ? today : openingDay2016);
		}
	}

	protected DateTime getDate()
	{
		BaseballTheater application = (BaseballTheater) getApplication();
		return application.getGameListDate();
	}

	private void setDate(DateTime newDate)
	{
		BaseballTheater application = (BaseballTheater) getApplication();
		application.setGameListDate(newDate);
		setTitle();
	}

	private void setTitle()
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DateTime date = getDate();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM d, yyyy");
		String titleDate = fmt.print(date);
		getSupportActionBar().setTitle(getTitle() + " - " + titleDate);
	}

	private void refreshCurrentView()
	{


		mSwipeRefreshLayout.setRefreshing(true);

		if (mGameListStatePagerAdapter == null)
		{
			mGameListStatePagerAdapter = new GameListStatePagerAdapter(getSupportFragmentManager(), getApplicationContext());
			mViewPager = (ViewPager) findViewById(R.id.game_pager);

			final float[] lastPositionOffset = {0};
			final boolean[] alreadySetDate = {false};
			final boolean[] alreadySetNoGamesFoundView = {false};
			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
			{
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
				{
					DateTime initialDate = mGameListStatePagerAdapter.getDateFromPosition(position);

					float diff = positionOffset - lastPositionOffset[0];
					boolean isIncrease = diff > 0.0;
					boolean crossedThreshold = (positionOffset > 0.5 && lastPositionOffset[0] <= 0.5) || (positionOffset < 0.5 && lastPositionOffset[0] >= 0.5);

					if (crossedThreshold)
					{
						DateTime newDate = new DateTime(initialDate);
						alreadySetDate[0] = true;
						if (isIncrease)
						{
							setDate(newDate.plusDays(1));
						}
						else if (!isIncrease)
						{
							setDate(newDate);
						}
					}
					else if (positionOffset == 0)
					{
						alreadySetDate[0] = false;
					}

					lastPositionOffset[0] = positionOffset;
					Log.d("diff", Float.toString(diff));
				}

				@Override
				public void onPageSelected(int position)
				{
					setDate(mGameListStatePagerAdapter.getDateFromPosition(position));
					alreadySetDate[0] = false;
				}

				@Override
				public void onPageScrollStateChanged(int state)
				{
					if (state == SCROLL_STATE_DRAGGING && !alreadySetNoGamesFoundView[0])
					{
						mNoGamesFoundView.setVisibility(GONE);
						alreadySetNoGamesFoundView[0] = true;
					}

					if (state == SCROLL_STATE_IDLE)
					{
						alreadySetNoGamesFoundView[0] = false;
						mNoGamesFoundView.setVisibility(mLastValidNoGamesVisibilityState);
					}

					enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
				}
			});
		}

		mViewPager.setAdapter(null);
		mViewPager.setAdapter(mGameListStatePagerAdapter);
		mGameListStatePagerAdapter.setDate(getDate());
		mViewPager.setCurrentItem((int) Math.floor(mGameListStatePagerAdapter.getCount() / 2));

		setTitle();
	}

	private void enableDisableSwipeRefresh(boolean enable)
	{
		if (mSwipeRefreshLayout != null)
		{
			mSwipeRefreshLayout.setEnabled(enable);
		}
	}

	private void initializeDrawer()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (LinearLayout) findViewById(R.id.left_drawer_list);
		mDrawerImage = (ImageView) findViewById(R.id.left_drawer_image);
		mPatreonProgress = (ProgressBar) findViewById(R.id.patreon_progress);
		mPatreon = (LinearLayout) findViewById(R.id.patreon);

		final String favTeamCode = mPrefs.getString("behavior_favorite_team", "none");
		String imageName = "team_splash_" + favTeamCode;
		int teamLogoResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

		TextView setFavoriteTeam = (TextView) findViewById(R.id.set_favorite_team);
		boolean hideSetFavoriteTeam = !favTeamCode.equals("none");
		setFavoriteTeam.setVisibility(hideSetFavoriteTeam ? GONE : VISIBLE);

		Picasso.with(context)
				.load(teamLogoResourceId)
				.placeholder(R.color.colorPlaceholder)
				.into(mDrawerImage);

		List<DrawerRowItem> listItems = new ArrayList<>(Arrays.asList(
				new DrawerRowItem("Settings", R.drawable.ic_settings),
				new DrawerRowItem("About", R.drawable.ic_info),
				new DrawerRowItem("Feedback", R.drawable.ic_feedback),
				new DrawerRowItem("Patreon Backers", R.drawable.ic_patreon)
		));

		mDrawerImage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, SettingsActivity.class);
				intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.BehaviorPreferenceFragment.class.getName());
				intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				context.startActivity(intent);
			}
		});

		LayoutInflater inflater = LayoutInflater.from(this);
		int index = 0;
		for (DrawerRowItem item : listItems) {
			View view  = inflater.inflate(R.layout.drawer_list_item, mDrawerList, false);
			TextView textView = (TextView) view.findViewById(R.id.title);
			ImageView imageView = (ImageView) view.findViewById(R.id.icon);

			textView.setText(item.mTitle);
			imageView.setImageResource(item.mResourceId);

			// set item content in view
			mDrawerList.addView(view);
			view.setTag(index);
			view.setOnClickListener(new DrawerItemClickListener(this));
			index++;
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close)
		{

			public void onDrawerClosed(View view)
			{
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
			}
		};

		mPatreon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String url = "https://www.patreon.com/jakelauer";
				CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
				CustomTabsIntent customTabsIntent = builder.build();
				customTabsIntent.launchUrl(context, Uri.parse(url));
			}
		});

		mDrawerLayout.addDrawerListener(mDrawerToggle);
		mDrawerToggle.setDrawerIndicatorEnabled(true);

		mPatreonProgress.setProgress(50);
	}

	private void initializeRefreshView()
	{
		final GameListActivity gameListActivity = this;
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshableGameList);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				gameListActivity.refreshCurrentView();
			}
		});
	}

	private void createFab()
	{
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				DatePickerFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
	}

	@Override
	public void onProgressUpdate(double progress)
	{
	}

	@Override
	public void onProgressFinished(Object objectInstance)
	{
		mSwipeRefreshLayout.setRefreshing(false);
		setTitle();
	}

	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			GameListActivity gameListActivity = (GameListActivity) getActivity();

			// Use the current date as the default date in the picker
			int year = gameListActivity.getDate().getYear();
			int month = gameListActivity.getDate().getMonthOfYear() - 1;
			int day = gameListActivity.getDate().getDayOfMonth();

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			String dateString = year + "/" + (month + 1) + "/" + day;
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");

			try
			{
				GameListActivity gameListActivity = (GameListActivity) getActivity();
				gameListActivity.setDate(DateTime.parse(dateString, fmt));
				gameListActivity.refreshCurrentView();
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (BaseballTheater.getSettingsChanged())
		{
			Intent intent = getIntent();
			finish();
			startActivity(intent);

			BaseballTheater.setSettingsChanged(false);
		}
	}

}
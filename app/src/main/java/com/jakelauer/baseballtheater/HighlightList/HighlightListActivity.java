package com.jakelauer.baseballtheater.HighlightList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.GameList.GameListActivity;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;

import java.io.IOException;

import icepick.Icepick;

public class HighlightListActivity extends AppCompatActivity implements ProgressActivity
{

	private RecyclerView recyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);

		setContentView(R.layout.activity_highlight_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initializeRefreshView();

		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);

		setTitleBar(gameSummary);

		this.refresh();
	}

	private void refresh()
	{
		mSwipeRefreshLayout.setRefreshing(true);

		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);
		GameDetailCreator detailCreator = new GameDetailCreator(gameSummary.gameDataDirectory, false);
		detailCreator.getHighlights(this);
	}

	private void setTitleBar(GameSummary gameSummary)
	{
		ActionBar actionBar = getSupportActionBar();

		String titleDate = gameSummary.dateObj().toString("MMMM d, yyyy");

		if (actionBar != null)
		{
			actionBar.setTitle(gameSummary.homeTeamName + " @ " + gameSummary.awayTeamName + " - " + titleDate);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void initializeRefreshView()
	{
		final HighlightListActivity highlightListActivity = this;
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshableGameList);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				highlightListActivity.refresh();
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

		HighlightsCollection highlightsCollection = (HighlightsCollection) objectInstance;

		TextView noHighlightsFoundView = (TextView) findViewById(R.id.no_highlights_found);

		if (highlightsCollection != null && highlightsCollection.highlights != null)
		{
			noHighlightsFoundView.setVisibility(View.GONE);

			showHighlights(highlightsCollection);
		}
		else
		{
			noHighlightsFoundView.setVisibility(View.VISIBLE);
		}

		mSwipeRefreshLayout.setRefreshing(false);
	}

	private void showHighlights(HighlightsCollection highlightsCollection)
	{
		recyclerView = (RecyclerView) findViewById(R.id.highlight_list);
		recyclerView.setAdapter(new HighlightRecyclerViewAdapter(this, highlightsCollection.highlights));

		int columns = 2;
		if (BaseballTheater.isSmallDevice())
		{
			columns = 1;
		}
		else if (BaseballTheater.isLargeDevice())
		{
			columns = 3;
		}

		GridLayoutManager glm = new GridLayoutManager(this, columns);
		recyclerView.setLayoutManager(glm);
	}


	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

}
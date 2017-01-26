package com.jakelauer.baseballtheater.HighlightList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;

import java.io.IOException;

import icepick.Icepick;

public class HighlightListActivity extends AppCompatActivity implements ProgressActivity {

	private RecyclerView recyclerView;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);

		setContentView(R.layout.activity_highlight_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);

		setTitleBar(gameSummary);
		showProgressDialog();

		recyclerView = (RecyclerView) findViewById(R.id.highlight_list);
		assert recyclerView != null;
		try {
			setupRecyclerView(recyclerView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showProgressDialog(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Loading");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	private void setTitleBar(GameSummary gameSummary) {
		ActionBar actionBar = getSupportActionBar();

		String titleDate = gameSummary.dateObj().toString("MMMM d, yyyy");

		if (actionBar != null) {
			actionBar.setTitle(gameSummary.homeTeamName + " @ " + gameSummary.awayTeamName + " - " + titleDate);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onProgressUpdate(double progress) {
	}

	@Override
	public void onProgressFinished(Object objectInstance) {

		HighlightsCollection highlightsCollection = (HighlightsCollection) objectInstance;

		TextView noHighlightsFoundView = (TextView) findViewById(R.id.no_highlights_found);

		if (highlightsCollection != null && highlightsCollection.highlights != null) {
			noHighlightsFoundView.setVisibility(View.GONE);

			showHighlights(highlightsCollection);
		} else {
			noHighlightsFoundView.setVisibility(View.VISIBLE);
		}

		progressDialog.dismiss();
	}

	private void showHighlights(HighlightsCollection highlightsCollection){
		recyclerView = (RecyclerView) findViewById(R.id.highlight_list);
		recyclerView.setAdapter(new HighlightRecyclerViewAdapter(this, highlightsCollection.highlights));

		GridLayoutManager glm = new GridLayoutManager(this, BaseballTheater.isSmallDevice() ? 1 : 2);
		recyclerView.setLayoutManager(glm);
	}


	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}

	private void setupRecyclerView(@NonNull final RecyclerView recyclerView) throws IOException {
		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);

		GameDetailCreator detailCreator = new GameDetailCreator(gameSummary.gameDataDirectory, false);

		detailCreator.getHighlights(this);
	}
}
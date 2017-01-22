package com.jakelauer.baseballtheater;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import icepick.Icepick;

public class HighlightListActivity extends AppCompatActivity implements ProgressActivity {

	public Date date = new Date();
	private RecyclerView recyclerView;
	private ProgressBar progressBar;
	private ProgressDialog progressDialog;

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);

		setContentView(R.layout.activity_game_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);

		String titleDate = new SimpleDateFormat("MMMM d, yyyy").format(date);
		getSupportActionBar().setTitle(gameSummary.homeTeamName + " @ " + gameSummary.awayTeamName + " - " + titleDate);

		recyclerView = (RecyclerView) findViewById(R.id.game_list);
		assert recyclerView != null;
		try {
			setupRecyclerView(recyclerView);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		if (findViewById(R.id.game_detail_container) != null) {
			mTwoPane = true;
		}

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setVisibility(View.GONE);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Loading");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void onProgressUpdate(double progress) {
		Integer progressInt = (int) progress * 100;
		if (progressBar != null) {
			progressBar.setProgress(progressInt);
		}
	}

	@Override
	public void onProgressFinished(Object objectInstance) {
		HighlightsCollection highlightsCollection = (HighlightsCollection) objectInstance;

		if(highlightsCollection != null && highlightsCollection.highlights != null) {
			recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(highlightsCollection.highlights));
		}

		progressDialog.dismiss();
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	private void setupRecyclerView(@NonNull final RecyclerView recyclerView) throws IOException {
		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);

		GameDetailCreator detailCreator = new GameDetailCreator(gameSummary.gameDataDirectory, false);

		detailCreator.getHighlights(this);
	}

	public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

		private final List<Highlight> mValues;

		public SimpleItemRecyclerViewAdapter(List<Highlight> highlights) {
			mValues = highlights;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.highlight_list_content, parent, false);

			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			Highlight highlight = mValues.get(position);

			holder.mItem = highlight;
			holder.mIdView.setText(highlight.headline);
			holder.mImageView.setImageDrawable(null);

			if (highlight.thumbs != null && highlight.thumbs.thumbs != null && highlight.thumbs.thumbs.size() > 0) {
				String thumb = highlight.thumbs.thumbs.get(highlight.thumbs.thumbs.size() - 3);

				Picasso.with(getApplicationContext())
						.load(thumb)
						.placeholder(R.color.colorPlaceholder)
						.into(holder.mImageView);
			}

			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = holder.mItem.urls.get(0);
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(browserIntent);
				}
			});
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public final View mView;
			public final TextView mIdView;
			public final ImageView mImageView;
			public Highlight mItem;

			public ViewHolder(View view) {
				super(view);
				mView = view;
				mIdView = (TextView) view.findViewById(R.id.id);
				mImageView = (ImageView) view.findViewById(R.id.highlight_list_thumb);
			}
		}
	}

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);


			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), R.style.DialogStyle, this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			String dateString = year + "/" + (month + 1) + "/" + day;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			try {
				HighlightListActivity gameListActivity = (HighlightListActivity) getActivity();
				RecyclerView recyclerView = (RecyclerView) gameListActivity.findViewById(R.id.game_list);

				gameListActivity.date = formatter.parse(dateString);
				gameListActivity.setupRecyclerView(recyclerView);
			} catch (ParseException | java.text.ParseException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
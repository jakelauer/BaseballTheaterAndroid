package com.jakelauer.baseballtheater.GameList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jakelauer.baseballtheater.HighlightListActivity;
import com.jakelauer.baseballtheater.HighlightListFragment;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection;
import com.jakelauer.baseballtheater.MlbDataServer.GameSummaryCreator;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameListActivity extends AppCompatActivity implements ProgressActivity {

	private String openingDay2016String = "20160403";
	private String openingDay2017String = "20170402";
	private Date openingDay2016;
	private Date openingDay2017;
	private Date today = new Date();
	private Date date;
	private Calendar cal = Calendar.getInstance();
	private RecyclerView recyclerView;
	private ProgressDialog progressDialog;

	private TableLayout mTableLayout;
	private GameListLineScore lineScore;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		try {
			openingDay2016 = new SimpleDateFormat("yyyyMMdd").parse(openingDay2016String);
			openingDay2017 = new SimpleDateFormat("yyyyMMdd").parse(openingDay2017String);

			this.setDate(today.after(openingDay2017) ? today : openingDay2016);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_game_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		String titleDate = new SimpleDateFormat("MMMM d, yyyy").format(date);
		getSupportActionBar().setTitle(getTitle() + " - " + titleDate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatePickerFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});

        recyclerView = (RecyclerView) findViewById(R.id.game_list);
        assert recyclerView != null;
        try {
            setupRecyclerView(recyclerView);
        }
        catch(IOException e){
            e.printStackTrace();
        }

		lineScore = new GameListLineScore();

        if (findViewById(R.id.game_detail_container) != null) {
            mTwoPane = true;
        }

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Loading");
		progressDialog.setCancelable(false);
		progressDialog.show();
    }

	public void setDate(Date newdate){
		date = newdate;
		cal.setTime(newdate);
	}

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) throws IOException {

        GameSummaryCreator gsCreator = new GameSummaryCreator();
        try {
            gsCreator.GetSummaryCollection(date, this);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onProgressUpdate(double progress) {
	}

	@Override
	public void onProgressFinished(Object objectInstance) {
		GameSummaryCollection gsCollection = (GameSummaryCollection) objectInstance;

		if(gsCollection != null && gsCollection.GameSummaries != null) {
			recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(gsCollection.GameSummaries));
		}

		String titleDate = new SimpleDateFormat("MMMM d, yyyy").format(date);
		getSupportActionBar().setTitle(getTitle() + " - " + titleDate);

		progressDialog.dismiss();
	}

	public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<GameSummary> mValues;

        public SimpleItemRecyclerViewAdapter(List<GameSummary> games) {
            mValues = games;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
			GameSummary gameItem = mValues.get(position);

			lineScore.generateLinescore(getApplicationContext(), holder.mLineScoreLayout, gameItem);

			holder.mItem = gameItem;
//            holder.mIdView.setText(gameItem.homeTeamName + " @ " + gameItem.awayTeamName);

			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				if (mTwoPane) {
					Bundle arguments = new Bundle();
					arguments.putInt(HighlightListFragment.ARG_ITEM_ID, holder.mItem.gamePk);
					HighlightListFragment fragment = new HighlightListFragment();
					fragment.setArguments(arguments);
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.game_detail_container, fragment)
							.commit();
				} else {
					Context context = v.getContext();
					Intent intent = new Intent(context, HighlightListActivity.class);
					intent.putExtra(HighlightListFragment.ARG_ITEM_ID, holder.mItem.gamePk);
					intent.putExtra(HighlightListFragment.ARG_GAME_SUMMARY, holder.mItem);

					context.startActivity(intent);
				}
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

			public final TableLayout mLineScoreLayout;

			public GameSummary mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
				mIdView = (TextView) view.findViewById(R.id.id);
				mLineScoreLayout = (TableLayout) view.findViewById(R.id.line_score);
            }
        }
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
				gameListActivity.date = formatter.parse(dateString);
				gameListActivity.setupRecyclerView((RecyclerView) gameListActivity.findViewById(R.id.game_list));
				gameListActivity.progressDialog.show();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.jakelauer.baseballtheater.GameList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jakelauer.baseballtheater.HighlightList.HighlightListActivity;
import com.jakelauer.baseballtheater.HighlightList.HighlightListFragment;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection;
import com.jakelauer.baseballtheater.MlbDataServer.GameSummaryCreator;
import com.jakelauer.baseballtheater.MlbDataServer.ProgressActivity;
import com.jakelauer.baseballtheater.R;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import icepick.Icepick;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

/**
 * Created by Jake on 1/22/2017.
 */

public class GameListActivityFragment extends Fragment implements ProgressActivity {
	public static String ARG_DATE = "date";
	public static String ARG_FORCE_REPLACE = "force_replace";

	private GameListActivity mActivity;
	private RecyclerView mRecyclerView;
	private GameListLineScore lineScore;
	private Date mDate;
	private Boolean mForceReplace = false;

	public static GameListActivityFragment newInstance(Date date, Boolean forceReplace){
		GameListActivityFragment newFrag = new GameListActivityFragment();
		Bundle args = new Bundle();
		args.putSerializable(GameListActivityFragment.ARG_DATE, date);
		args.putBoolean(GameListActivityFragment.ARG_FORCE_REPLACE, forceReplace);
		newFrag.setArguments(args);
		return newFrag;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		Icepick.restoreInstanceState(this, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		RecyclerView rootView = (RecyclerView) inflater.inflate(R.layout.game_list, container, false);

		mRecyclerView = rootView;

		mActivity = (GameListActivity) getActivity();

		Bundle args = getArguments();
		mDate = (Date) args.getSerializable(GameListActivityFragment.ARG_DATE);
		mForceReplace = args.getBoolean(GameListActivityFragment.ARG_FORCE_REPLACE);

		try {
			setupRecyclerView(mRecyclerView);
		} catch (IOException e) {
			e.printStackTrace();
		}

		lineScore = new GameListLineScore();

		return rootView;
	}

	private void setupRecyclerView(@NonNull final RecyclerView recyclerView) throws IOException {
		GameSummaryCreator gsCreator = new GameSummaryCreator();
		try {
			gsCreator.GetSummaryCollection(mDate, this);
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

		TextView noGamesFoundView = (TextView) getActivity().findViewById(R.id.no_games_found);

		if(gsCollection != null && gsCollection.GameSummaries != null) {
			noGamesFoundView.setVisibility(View.GONE);

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			final String favTeamCode = prefs.getString("behavior_favorite_team", "none");
			Collections.sort(gsCollection.GameSummaries, new Comparator<GameSummary>() {
				@Override
				public int compare(GameSummary o1, GameSummary o2) {
					int o1FavTeam = o1.homeFileCode.equals(favTeamCode) || o1.awayFileCode.equals(favTeamCode) ? 0 : 1;
					int o2FavTeam = o2.homeFileCode.equals(favTeamCode) || o2.awayFileCode.equals(favTeamCode) ? 0 : 1;

					if(o1FavTeam == 0 || o2FavTeam == 0){
						return o1FavTeam - o2FavTeam;
					}

					int o1NullSort = o1.linescore != null ? 0 : 1;
					int o2NullSort = o2.linescore != null ? 0 : 1;

					return o1NullSort - o2NullSort;
				}
			});

			mRecyclerView.setAdapter(new GameListActivityFragment.SimpleItemRecyclerViewAdapter(gsCollection.GameSummaries));
		}
		else{
			noGamesFoundView.setVisibility(View.VISIBLE);
		}

		mActivity.onProgressFinished(objectInstance);
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

			holder.mLineScoreLayout.removeAllViews();

			lineScore.generateLinescore(holder.mLineScoreLayout, gameItem);

			holder.mItem = gameItem;

			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Context context = getActivity();
					Intent intent = new Intent(context, HighlightListActivity.class);
					intent.putExtra(HighlightListFragment.ARG_ITEM_ID, holder.mItem.gamePk);
					intent.putExtra(HighlightListFragment.ARG_GAME_SUMMARY, holder.mItem);

					context.startActivity(intent);
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
}

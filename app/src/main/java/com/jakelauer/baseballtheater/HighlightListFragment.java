package com.jakelauer.baseballtheater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link HighlightListActivity}
 * on handsets.
 */
public class HighlightListFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "gamePk";
    public static final String ARG_GAME_SUMMARY = "gamePk";

    /**
     * The dummy content this fragment is presenting.
     */
    public GameSummary getGameSummary(){
        return (GameSummary) getArguments().getSerializable(ARG_GAME_SUMMARY);
    }

    private int gamePk;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HighlightListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.highlight_list, container, false);

        Bundle args = getArguments();

        GameSummary gameSummary = this.getGameSummary();

        if (args.containsKey(ARG_ITEM_ID)) {
            this.gamePk = args.getInt(ARG_ITEM_ID);
        }
        // Show the dummy content as text in a TextView.
        if (gameSummary != null) {
            ((TextView) rootView.findViewById(R.id.highlight_list)).setText(gameSummary.homeTeamName);

          /*  Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(gameSummary.awayTeamName + "@" + gameSummary.homeTeamName);
            }*/
        }

        return rootView;
    }
}

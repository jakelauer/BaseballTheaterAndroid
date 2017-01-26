package com.jakelauer.baseballtheater.HighlightList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.R;

import icepick.Icepick;

public class HighlightListFragment extends Fragment {
    public static final String ARG_ITEM_ID = "gamePk";
    public static final String ARG_GAME_SUMMARY = "gameSummary";

    private GameSummary gameSummary;

    public GameSummary getGameSummary(){
        return (GameSummary) getArguments().getSerializable(ARG_GAME_SUMMARY);
    }

    public HighlightListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.highlight_list, container, false);

        return rootView;
    }
}

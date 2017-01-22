package com.jakelauer.baseballtheater.GameList;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Inning;
import com.jakelauer.baseballtheater.R;

/**
 * Created by Jake on 1/20/2017.
 */

public class GameListLineScore {
	public void generateLinescore(Context context, TableLayout lineScoreLayout, GameSummary gameItem){

		TableRow labels = (TableRow) lineScoreLayout.findViewById(R.id.line_score_labels);
		TableRow teamAway = (TableRow) lineScoreLayout.findViewById(R.id.line_score_team_away);
		TableRow teamHome = (TableRow) lineScoreLayout.findViewById(R.id.line_score_team_home);

		TextView status = new TextView(context);
		status.setText(gameItem.status.status);
		status.setTypeface(status.getTypeface(), Typeface.BOLD);

		TextView awayTeamName = new TextView(context);
		awayTeamName.setText(gameItem.awayTeamName);
		awayTeamName.setTypeface(awayTeamName.getTypeface(), Typeface.BOLD);

		TextView homeTeamName = new TextView(context);
		homeTeamName.setText(gameItem.homeTeamName);
		homeTeamName.setTypeface(homeTeamName.getTypeface(), Typeface.BOLD);

		labels.addView(status);
		teamAway.addView(awayTeamName);
		teamHome.addView(homeTeamName);

		status.setPadding(20,20,20,20);
		teamAway.setPadding(20,20,20,20);
		teamHome.setPadding(20,20,20,20);

		if(gameItem.linescore != null && gameItem.linescore.innings != null) {
			int inningsCount = gameItem.linescore.innings.size();

			for (int i = 1; i <= 9; i++) {
				int currentInning = inningsCount - (9 - i);
				Inning inningData = gameItem.linescore.innings.get(currentInning - 1);

				TextView inningLabel = new TextView(context);
				inningLabel.setTypeface(inningLabel.getTypeface(), Typeface.BOLD);
				inningLabel.setText(Integer.toString(currentInning));

				TextView inningAway = new TextView(context);
				inningAway.setText(inningData.away);

				TextView inningHome = new TextView(context);
				inningHome.setText(inningData.home != null ? inningData.home : "X");

				labels.addView(inningLabel);
				teamAway.addView(inningAway);
				teamHome.addView(inningHome);

				inningLabel.setPadding(20,20,20,20);
			}
		}
	}
}

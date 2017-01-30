package com.jakelauer.baseballtheater.GameList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jakelauer.baseballtheater.Baseballtheater;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HomeAway;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Inning;
import com.jakelauer.baseballtheater.Utility;
import com.jakelauer.baseballtheater.R;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import static com.jakelauer.baseballtheater.Utility.bold;
import static dk.nodes.okhttputils.error.HttpErrorManager.context;

/**
 * Created by Jake on 1/20/2017.
 */

public class GameListLineScore {

	private final float mScale = context.getResources().getDisplayMetrics().density;
	private boolean hideScores;

	public TableLayout generateLinescore(TableLayout lineScoreTableLayout, GameSummary gameItem){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		DateTime today = new DateTime();
		int hoursBetween = Hours.hoursBetween(gameItem.dateObj(), today).getHours();
		boolean hide_todays_scores = prefs.getBoolean("behavior_hide_todays_scores", false);
		hideScores = hide_todays_scores && hoursBetween < 24;

		String favTeamCode = prefs.getString("behavior_favorite_team", "");
		lineScoreTableLayout.setBackground(null);
		if(gameItem.awayFileCode.equals(favTeamCode) || gameItem.homeFileCode.equals(favTeamCode)){
			lineScoreTableLayout.setBackgroundResource(R.color.featuredGame);
		}

		TableRow labels = new TableRow(context);
		TableRow teamAway = new TableRow(context);
		TableRow teamHome = new TableRow(context);

		TableLayout.LayoutParams trlp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

		labels.setLayoutParams(trlp);
		teamAway.setLayoutParams(trlp);
		teamHome.setLayoutParams(trlp);

		lineScoreTableLayout.addView(labels);
		lineScoreTableLayout.addView(teamAway);
		lineScoreTableLayout.addView(teamHome);

		setBaseSize(labels);
		setBaseSize(teamAway);
		setBaseSize(teamHome);

		labels.setBackgroundResource(R.color.colorPlaceholder);

		setTeamsAndStatus(gameItem, labels, teamAway, teamHome);
		createLineScore(gameItem, labels, teamAway, teamHome);

		return lineScoreTableLayout;
	}

	private void setTeamsAndStatus(GameSummary gameItem, TableRow labels, TableRow teamAway, TableRow teamHome){
		LineScoreTextView status = new LineScoreTextView(context);
		status.setText(gameItem.status.status);
		bold(status);

		labels.addView(status);

		setTeamName(gameItem.awayTeamName, gameItem.awayFileCode, teamAway);
		setTeamName(gameItem.homeTeamName, gameItem.homeFileCode, teamHome);
	}

	private void setTeamName(String teamName, String teamCode, TableRow row){
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);

		LineScoreTextView teamNameView = new LineScoreTextView(context);
		String teamDisplayName = Baseballtheater.isSmallDevice()
				? teamCode.toUpperCase()
				: teamName;
		teamNameView.setText(teamDisplayName);
		teamNameView.setTypeface(teamNameView.getTypeface(), Typeface.BOLD);

		String imageName = "team_logo_" + teamCode;
		ImageView teamLogoView = new ImageView(context);
		int teamLogoResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

		if(teamLogoResourceId == 0){
			teamLogoResourceId = R.drawable.team_logo_none;
		}

		Picasso.with(context)
				.load(teamLogoResourceId)
				.into(teamLogoView);

		linearLayout.addView(teamLogoView);
		linearLayout.addView(teamNameView);
		row.addView(linearLayout);

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
		lp.width = getPixels(Baseballtheater.isSmallDevice() ? 55 : 90);
		lp.setMargins(0, 0, getPixels(5), 0);
		linearLayout.setLayoutParams(lp);

		LinearLayout.LayoutParams logoLp = (LinearLayout.LayoutParams) teamLogoView.getLayoutParams();
		logoLp.height = ViewGroup.LayoutParams.MATCH_PARENT;
		logoLp.width = getPixels(30);
		teamLogoView.setLayoutParams(logoLp);

		teamLogoView.setScaleType(ImageView.ScaleType.FIT_START);
	}

	private void createLineScore(GameSummary gameItem, TableRow labels, TableRow teamAway, TableRow teamHome) {
		if (gameItem.linescore != null && gameItem.linescore.innings != null) {
			int inningsCount = gameItem.linescore.innings.size();

			int maxInningCount = Math.min(9, gameItem.linescore.innings.size());
			for (int i = 1; i <= maxInningCount; i++) {
				int currentInning = inningsCount - (maxInningCount - i);
				Inning inningData = gameItem.linescore.innings.get(currentInning - 1);

				LineScoreTextView inningLabel = new LineScoreTextView(context);
				bold(inningLabel);
				inningLabel.setText(Integer.toString(currentInning));

				LineScoreTextView inningAway = new LineScoreTextView(context);
				inningAway.setText(hideScores ? "X" : inningData.away);

				LineScoreTextView inningHome = new LineScoreTextView(context);
				String inningValue = inningData.home != null ? inningData.home : "X";
				inningHome.setText(hideScores ? "X" : inningValue);

				labels.addView(inningLabel);
				teamAway.addView(inningAway);
				teamHome.addView(inningHome);
			}

			addSeparator(labels);
			addSeparator(teamHome);
			addSeparator(teamAway);
		}

		LineScoreTextView runsLabel = new LineScoreTextView(context);
		LineScoreTextView hitsLabel = new LineScoreTextView(context);
		LineScoreTextView errorsLabel = new LineScoreTextView(context);

		Utility.bold(runsLabel);
		Utility.bold(hitsLabel);
		Utility.bold(errorsLabel);

		labels.addView(runsLabel);
		labels.addView(hitsLabel);
		labels.addView(errorsLabel);

		if (gameItem.linescore != null) {
			LineScoreTextView runsHome = new LineScoreTextView(context);
			LineScoreTextView runsAway = new LineScoreTextView(context);
			LineScoreTextView hitsHome = new LineScoreTextView(context);
			LineScoreTextView hitsAway = new LineScoreTextView(context);
			LineScoreTextView errorsHome = new LineScoreTextView(context);
			LineScoreTextView errorsAway = new LineScoreTextView(context);

			runsLabel.setText("R");
			hitsLabel.setText("H");
			errorsLabel.setText("E");

			setHomeAwayText(runsHome, runsAway, gameItem.linescore.runs, teamHome, teamAway);
			setHomeAwayText(hitsHome, hitsAway, gameItem.linescore.hits, teamHome, teamAway);
			setHomeAwayText(errorsHome, errorsAway, gameItem.linescore.errors, teamHome, teamAway);
		}
	}

	private void setBaseSize(TableRow tableRow){
		int v20 = getPixels(5);
		tableRow.setPadding(v20,v20,v20,v20);
		tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
	}

	private void setHomeAwayText(LineScoreTextView homeText, LineScoreTextView awayText, HomeAway values, TableRow rowHome, TableRow rowAway){
		homeText.setText(hideScores ? "X" : values.home);
		awayText.setText(hideScores ? "X" : values.away);

		rowHome.addView(homeText);
		rowAway.addView(awayText);
	}

	private void addSeparator(TableRow row){
		ImageView separator = new ImageView(context);
		separator.setBackgroundResource(R.color.colorAccent2);

		row.addView(separator);

		TableRow.LayoutParams lp = new TableRow.LayoutParams(3, ViewGroup.LayoutParams.MATCH_PARENT);
		lp.setMargins(getPixels(15), 0, getPixels(15), 0);
		separator.setLayoutParams(lp);
	}

	private int getPixels(int dps){
		return Utility.getPixels(dps);
	}

	class LineScoreTextView extends TextView
	{
		public LineScoreTextView(Context context){
			super(context);

			setTextColor(ContextCompat.getColor(context, R.color.textDefault));
			setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixels(Baseballtheater.isSmallDevice() ? 11 : 12));
		}
	}
}

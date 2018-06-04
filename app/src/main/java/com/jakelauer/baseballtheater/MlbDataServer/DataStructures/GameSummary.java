package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Jake on 1/13/2017.
 */

@Root(name = "game", strict = false)
public class GameSummary implements Serializable
{
	@Attribute
	public String id;

	@Attribute(name = "game_pk")
	public int gamePk;

	@Attribute(name = "time_date")
	public String date;

	public DateTime dateObj()
	{
		DateTimeFormatter f = DateTimeFormat.forPattern("yyyy/MM/dd h:m");
		return f.parseDateTime(date);
	}

	public LocalDateTime localDateObj()
	{
		DateTimeFormatter withoutTzFormatter = DateTimeFormat.forPattern("yyyy/MM/dd h:m");
		DateTimeFormatter withTzFormatter = DateTimeFormat.forPattern("yyyy/MM/dd h:m Z");
		DateTimeZone zone = DateTimeZone.forID("America/New_York");
		DateTimeZone localZone = DateTimeZone.getDefault();

		DateTime parsedDateTime = withoutTzFormatter.parseDateTime(date);
		int offsetMs = zone.getOffset(parsedDateTime);
		String offsetString = offsetMsToOffsetString(offsetMs);
		LocalDateTime convertedTime = withTzFormatter.parseDateTime(date + " " + offsetString).withZone(localZone).toLocalDateTime();
		return convertedTime.plusHours(12);
	}

	private String offsetMsToOffsetString(int offsetMs)
	{
		int offsetHours = (offsetMs / 1000 / 60 / 60);
		int offsetHours100 = offsetHours * 100;
		boolean isNegative = offsetHours < 0;
		String offsetString = offsetHours100 + "";

		if (Math.abs(offsetHours) < 10)
		{
			offsetString = isNegative
					? "-0" + Math.abs(offsetHours100)
					: "0" + offsetHours100;
		}

		return offsetString;
	}

	@Attribute(name = "game_type")
	public String gameType;

	@Attribute(name = "time")
	public String eventTime;

	@Attribute(name = "ampm")
	public String eventTimeAmPm;

	@Attribute(name = "time_zone")
	public String timeZone;

	@Element
	public GameStatus status;

	@Attribute
	public String league;

	@Attribute(name = "away_name_abbrev")
	public String awayTeamNameAbbr;

	@Attribute(name = "away_team_name")
	public String awayTeamName;

	@Attribute(name = "away_team_city")
	public String awayTeamCity_actual;

	@Attribute(name = "away_file_code")
	public String awayFileCode;

	@Attribute(name = "home_name_abbrev")
	public String homeTeamNameAbbr;

	@Attribute(name = "home_team_name")
	public String homeTeamName;

	@Attribute(name = "home_file_code")
	public String homeFileCode;

	@Attribute(name = "home_team_city")
	public String homeTeamCity_actual;

	@Attribute(name = "game_data_directory")
	public String gameDataDirectory;

	@Element(required = false)
	public Linescore linescore;

	@Attribute
	public String home_win;

	@Attribute
	public String home_loss;

	@Attribute
	public String away_win;

	@Attribute
	public String away_loss;

	@Attribute
	public String gameday_sw;

	public boolean getIsSpringTraining()
	{
		return this.gameday_sw.equals("Y");
	}

	public String getAwayTeamCity()
	{
		return getCity(awayTeamCity_actual);
	}

	public String getHomeTeamCity()
	{
		return getCity(homeTeamCity_actual);
	}

	private String getCity(final String cityName)
	{
		if (cityName.toLowerCase().indexOf("la ") == 0)
		{
			return "Los Angeles";
		}

		if (cityName.toLowerCase().indexOf("chi") == 0)
		{
			return "Chicago";
		}

		if (cityName.toLowerCase().indexOf("ny") == 0)
		{
			return "New York";
		}

		return cityName;
	}

	public boolean getGameIsOver()
	{
		return (status.status.equals("Final")
				|| status.ind.equals("F")
				|| status.status.equals("Game Over")
				|| status.ind.equals("O"));
	}

	public String getCurrentInning()
	{
		if (getGameIsOver())
		{
			return status.status;
		}

		if (!TextUtils.isEmpty(status.reason))
		{
			return status.status + " (" + status.reason + ")";
		}

		return !status.inning_state.isEmpty()
				? status.inning_state + " " + status.inning
				: getStatusTime();
	}

	private String getStatusTime()
	{
		return localDateObj().toString("hh:mm a");
	}
}

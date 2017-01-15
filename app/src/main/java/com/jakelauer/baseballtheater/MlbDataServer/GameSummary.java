package com.jakelauer.baseballtheater.MlbDataServer;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by Jake on 1/13/2017.
 */

@Root(name = "game")
public class GameSummary {
    @Attribute
    public String id;

    @Attribute(name = "game_pk")
    public int gamePk;

    @Attribute(name = "time_date")
    public String date;

    @Attribute(name = "game_type")
    public String gameType;

    @Attribute(name = "time")
    public String eventTime;

    @Attribute(name = "ampm")
    public String eventTimeAmPm;

    @Attribute(name = "time_zone")
    public String timeZone;

    //[XmlElement("status")
    //public GameStatus status;

    @Attribute
    public String league;

    @Attribute
    public String inning;

    @Attribute(name = "away_name_abbrev")
    public String awayTeamNameAbbr;

    @Attribute(name = "away_team_name")
    public String awayTeamName;

    @Attribute(name = "away_file_code")
    public String awayFileCode;

    @Attribute(name = "home_name_abbrev")
    public String homeTeamNameAbbr;

    @Attribute(name = "home_team_name")
    public String homeTeamName;

    @Attribute(name = "home_file_code")
    public String homeFileCode;

    @Attribute(name = "game_data_directory")
    public String gameDataDirectory;

    //@JsonProperty
    //public Linescore linescore;
}

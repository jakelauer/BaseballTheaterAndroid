package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.BoxScore;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Created by Jake on 2/7/2018.
 */

@Root(name =  "boxscore", strict = false)
public class BoxScoreData
{
	@Attribute
	public String game_id;

	@Attribute
	public String game_pk;

	@Attribute
	public String venue_id;

	@Attribute
	public String venue_name;

	@Attribute
	public String away_team_code;

	@Attribute
	public String home_team_code;

	@Attribute
	public String away_id;

	@Attribute
	public String home_id;

	@Attribute
	public String away_fname;

	@Attribute
	public String home_fname;

	@Attribute
	public String away_sname;

	@Attribute
	public String home_sname;

	@Attribute
	public String date;

	@Attribute
	public String away_wins;

	@Attribute
	public String away_loss;

	@Attribute
	public String home_wins;

	@Attribute
	public String home_loss;

	@Attribute
	public String status_ind;

	@ElementList(name = "pitching")
	public Pitching pitching;

	@ElementList(name = "batting")
	public Batting batting;

	@Element
	public String game_info;
}



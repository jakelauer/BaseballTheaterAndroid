package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.BoxScore.Player;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "atbat", strict = false)
public class AtBat
{
	@Attribute
	public int num;

	@Attribute
	public int b;

	@Attribute
	public int s;

	@Attribute
	public int o;

	@Attribute
	public String start_tfs;

	@Attribute
	public String start_tfs_zulu;

	@Attribute
	public String end_tfs_zulu;

	@Attribute
	public int batter;

	//$todo
	public Player batterData;

	@Attribute
	public String stand;

	@Attribute
	public String b_height;

	@Attribute
	public String pitcher;

	@Attribute
	public String p_throws;

	@Attribute
	public String des;

	@Attribute
	public String event_num;

	@Attribute
	public String event;

	@Attribute
	public String home_team_runs;

	@Attribute
	public String away_team_runs;

	@ElementList(required = false, inline = true, entry = "pitch")
	public List<Pitch> pitches;
}

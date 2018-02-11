package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.BoxScore.Player;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Jake on 2/8/2018.
 */

@Root(name = "game", strict = false)
public class InningsGame
{
	@Attribute
	public String atBat;

	@Attribute
	public String deck;

	@Attribute
	public String hole;

	@Attribute
	public String ind;

	@ElementList(name = "inning", inline = true, required = false)
	public List<Inning> innings;
}

@Root(name = "inning", strict = false)
class Inning
{
	@Attribute
	public String num;

	@Attribute
	public String away_team;

	@Attribute
	public String home_team;

	@Attribute
	public Next next;

	@Element(required = false)
	public InningHalf top;

	@Element(required = false)
	public InningHalf bottom;
}

enum Next
{
	Y,
	N
}

@Root(strict = false)
class InningHalf
{
	@ElementList(name = "atbat", inline = true, required = false)
	public List<AtBat> atbat;
}

@Root(name = "atbat", strict = false)
class AtBat
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

	@ElementList(required = false)
	public List<Pitch> pitches;

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
}

@Root(name = "pitch", strict = false)
class Pitch
{
	@Attribute
	public String ax;

	@Attribute
	public String ay;

	@Attribute
	public String az;

	@Attribute
	public String break_angle;

	@Attribute
	public String break_length;

	@Attribute
	public String break_y;

	@Attribute
	public String cc;

	@Attribute
	public String code;

	@Attribute
	public String des;

	@Attribute
	public String end_speed;

	@Attribute
	public String event_num;

	@Attribute
	public String id;

	@Attribute
	public String mt;

	@Attribute
	public String pfx_x;

	@Attribute
	public String pfx_z;

	@Attribute
	public String pitch_type;

	public String get_pitch_type_detail()
	{
		String detail = "Unknown pitch";
		switch (pitch_type)
		{
			case "CH":
				detail = "Changeup";
				break;
			case "CU":
				detail = "Curve";
				break;
			case "EP":
				detail = "Eephus";
				break;
			case "FC":
				detail = "Cutter";
				break;
			case "FF":
				detail = "Four-seam Fastball";
				break;
			case "FO":
			case "PO":
				detail = "Pitch Out";
				break;
			case "FS":
			case "SI":
				detail = "Sinker";
				break;
			case "FT":
				detail = "Two-seam Fastball";
				break;
			case "KC":
				detail = "Knuckle Curve";
				break;
			case "KN":
				detail = "Knuckleball";
				break;
			case "SF":
				detail = "Split-finger Fastball";
				break;
			case "SL":
				detail = "Slider";
				break;

			default:
				break;
		}

		return detail;
	}

	@Attribute
	public String play_guid;

	@Attribute
	public String px;

	@Attribute
	public String pz;

	@Attribute
	public String spin_dir;

	@Attribute
	public String spin_rate;

	@Attribute
	public String start_speed;

	@Attribute
	public String sv_id;

	@Attribute
	public String sz_bot;

	@Attribute
	public String sz_top;

	@Attribute
	public String tfs;

	@Attribute
	public String tfs_zulu;

	@Attribute
	public String type;

	@Attribute
	public String type_confidence;

	@Attribute
	public String vx0;

	@Attribute
	public String vy0;

	@Attribute
	public String vz0;

	@Attribute
	public String x;

	@Attribute
	public String x0;

	@Attribute
	public String y;

	@Attribute
	public String y0;

	@Attribute
	public String z0;

	@Attribute
	public String zone;
}
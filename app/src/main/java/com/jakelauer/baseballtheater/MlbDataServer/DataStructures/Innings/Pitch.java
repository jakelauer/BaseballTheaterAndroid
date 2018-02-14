package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "pitch", strict = false)
public class Pitch
{
	@Attribute(required = false)
	public String ax;

	@Attribute(required = false)
	public String ay;

	@Attribute(required = false)
	public String az;

	@Attribute(required = false)
	public String break_angle;

	@Attribute(required = false)
	public String break_length;

	@Attribute(required = false)
	public String break_y;

	@Attribute(required = false)
	public String cc;

	@Attribute(required = false)
	public String code;

	@Attribute(required = false)
	public String des;

	@Attribute(required = false)
	public String end_speed;

	@Attribute(required = false)
	public String event_num;

	@Attribute(required = false)
	public String id;

	@Attribute(required = false)
	public String mt;

	@Attribute(required = false)
	public String pfx_x;

	@Attribute(required = false)
	public String pfx_z;

	@Attribute(required = false)
	public String pitch_type;

	public String getPitchTypeDetail()
	{
		String detail = "Unknown pitch";
		if(pitch_type != null)
		{
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
		}

		return detail;
	}

	@Attribute(required = false)
	public String play_guid;

	@Attribute(required = false)
	public String px;

	@Attribute(required = false)
	public String pz;

	@Attribute(required = false)
	public String spin_dir;

	@Attribute(required = false)
	public String spin_rate;

	@Attribute(required = false)
	public String start_speed;

	@Attribute(required = false)
	public String sv_id;

	@Attribute(required = false)
	public String sz_bot;

	@Attribute(required = false)
	public String sz_top;

	@Attribute(required = false)
	public String tfs;

	@Attribute(required = false)
	public String tfs_zulu;

	@Attribute(required = false)
	public String type;

	@Attribute(required = false)
	public String type_confidence;

	@Attribute(required = false)
	public String vx0;

	@Attribute(required = false)
	public String vy0;

	@Attribute(required = false)
	public String vz0;

	@Attribute(required = false)
	public String x;

	@Attribute(required = false)
	public String x0;

	@Attribute(required = false)
	public String y;

	@Attribute(required = false)
	public String y0;

	@Attribute(required = false)
	public String z0;

	@Attribute(required = false)
	public String zone;
}

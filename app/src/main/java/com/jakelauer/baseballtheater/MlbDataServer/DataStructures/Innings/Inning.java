package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "inning", strict = false)
public class Inning
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

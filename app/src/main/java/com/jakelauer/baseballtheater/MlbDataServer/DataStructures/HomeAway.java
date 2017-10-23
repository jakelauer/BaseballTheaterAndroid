package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public class HomeAway implements Serializable
{
	@Attribute(name = "home", required = false)
	public String home;

	@Attribute(name = "away", required = false)
	public String away;
}

package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Jake on 1/14/2017.
 */

@Root(name = "game", strict = false)
public class GameCenter implements Serializable
{
	@Attribute
	public String status;

	@Attribute
	public String id;

	@Element
	public String venueShort;

	@Element
	public String venueLong;

	@Element
	public Wrap wrap;

	@Element(name = "recaps")
	public Wrap recaps;
}


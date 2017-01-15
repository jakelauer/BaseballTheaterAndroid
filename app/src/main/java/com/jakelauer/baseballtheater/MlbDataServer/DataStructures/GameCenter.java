package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Jake on 1/14/2017.
 */

@Root
public class GameCenter implements Serializable {
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
}

@Root
class Wrap implements Serializable{
	@Element
	public Mlb mlb;
}

@Root
class Mlb implements Serializable
{
	@Element
	public String headline;

	@Element
	public String blurb;
}
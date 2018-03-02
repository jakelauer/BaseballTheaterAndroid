package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public class Mlb implements Serializable
{
	@Element(required = false)
	public String headline;

	@Element(required = false)
	public String blurb;

	@Element(required = false)
	public GcUrl url;
}

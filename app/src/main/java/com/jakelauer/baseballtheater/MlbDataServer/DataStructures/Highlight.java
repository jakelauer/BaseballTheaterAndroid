package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jake on 1/14/2017.
 */

@Root(name = "media", strict = false)
public class Highlight implements Serializable
{
	@Attribute(required = false)
	public String type;

	@Attribute(required = false)
	public int id;

	@Attribute(required = false)
	public String date;

	@Element(required = false)
	public String headline;

	@Element(required = false)
	public String blurb;

	@Element(required = false)
	public String bigblurb;

	@Element(required = false)
	public String duration;

	public Long durationMilliseconds()
	{
		String[] pieces = duration.split(":");
		int h = Integer.valueOf(pieces[0]);
		int m = Integer.valueOf(pieces[1]);
		int s = Integer.valueOf(pieces[2]);

		Long duration = (s * 1000L) + (m * 60 * 1000) + (h * 60 * 60 * 1000);

		return duration;
	}

	@ElementList(entry = "url", inline = true)
	public List<String> urls;

	//@Element(required = false)
	//public String thumb;

	@Element(name = "thumbnails")
	public Thumbs thumbs;

	@ElementList(entry = "keyword")
	public List<Keyword> keywords;

	@Attribute(required = false)
	public boolean condensed;

	@Attribute(required = false)
	public boolean recap;
}


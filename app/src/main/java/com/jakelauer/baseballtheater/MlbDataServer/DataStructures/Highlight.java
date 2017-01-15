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
public class Highlight implements Serializable {
	@Attribute
	public String type;

	@Attribute
	public long id;

	@Attribute
	public String date;

	@Element
	public String headline;

	@Element
	public String blurb;

	@Element
	public String duration;

	@ElementList(name = "url", inline = true)
	public List<String> urls;

	@Element
	public String thumb;

/*	[XmlArray("thumbnails")
			[XmlArrayItem("thumb")
	public String[] Thumbs;

	[XmlArray("keywords")
			[XmlArrayItem("keyword")
	public Keyword[] Keywords;*/

	@Attribute
	public Boolean condensed;

	@Attribute
	public Boolean recap;
}

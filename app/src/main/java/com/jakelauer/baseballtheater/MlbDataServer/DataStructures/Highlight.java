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
	public String duration;

	@ElementList(entry = "url", inline = true)
	public List<String> urls;

	//@Element(required = false)
	//public String thumb;

	@Element(name = "thumbnails")
	public Thumbs thumbs;

	/*[XmlArray("keywords")
			[XmlArrayItem("keyword")
	public Keyword[] Keywords;*/

	@Attribute(required = false)
	public boolean condensed;

	@Attribute(required = false)
	public boolean recap;
}


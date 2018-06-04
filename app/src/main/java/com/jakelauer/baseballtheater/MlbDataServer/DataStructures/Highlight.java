package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.joda.time.DateTime;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 1/14/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Root(name = "media", strict = false)
public class Highlight implements Serializable
{
	@JsonProperty
	@Attribute(required = false)
	public String type;

	@JsonProperty
	@Attribute(required = false)
	public int id;

	@JsonProperty
	@Attribute(required = false)
	public String date;

	public DateTime dateObj()
	{
		return DateTime.parse(date);
	}

	@JsonProperty
	@Element(required = false)
	public String headline;

	@JsonProperty
	@Element(required = false)
	public String blurb;

	@JsonProperty
	@Element(required = false)
	public String bigblurb;

	@JsonProperty
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

	@JsonProperty("url")
	public List<String> url;

	//@Element(required = false)
	//public String thumb;

	@Element(name = "thumbnails")
	public Thumbs thumbs;

	@JsonProperty
	public List<String> thumbnails;

	@JsonProperty
	@ElementList(entry = "keyword")
	public List<Keyword> keywords;

	@JsonProperty
	@Attribute(required = false)
	public boolean condensed;

	@JsonProperty
	@Attribute(required = false)
	public boolean recap;

	private DateTime parseDateTimeMsft(String dateString)
	{
		DateTime result = null;

		if (dateString != null)
		{
			Pattern datePattern = Pattern.compile("^/Date\\((\\d+)([+-]\\d+)?\\)/$");
			Matcher m = datePattern.matcher(dateString);
			if (m.matches())
			{
				Long l = java.lang.Long.parseLong(m.group(1));
				result = new DateTime(l);
			}
		}

		return result;
	}
}


package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Root(name = "thumbnails", strict = false)
public class HighlightThumbs implements Serializable
{
	@JsonProperty
	@Attribute(required = false)
	public String high;

	@JsonProperty
	@Attribute(required = false)
	public String med;

	@JsonProperty
	@Attribute(required = false)
	public String low;
}


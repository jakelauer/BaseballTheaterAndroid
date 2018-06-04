package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Jake on 1/14/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Root(strict = false)
public class Keyword  implements Serializable {

	@JsonProperty("Type")
	@Attribute
	public String type;

	@Attribute
	@JsonProperty("Value")
	public String value;
}

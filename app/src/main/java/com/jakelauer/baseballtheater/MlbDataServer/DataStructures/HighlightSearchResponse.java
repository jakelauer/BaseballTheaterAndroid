package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by jlauer on 2/21/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HighlightSearchResponse
{
	public List<HighlightSearchResult> highlights;
}

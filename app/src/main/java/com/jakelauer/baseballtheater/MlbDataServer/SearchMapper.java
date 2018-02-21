package com.jakelauer.baseballtheater.MlbDataServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightSearchResult;

import java.io.IOException;

/**
 * Created by jlauer on 2/21/2018.
 */

public class SearchMapper
{
	public static HighlightSearchResult[] map(String json)
	{
		ObjectMapper mapper = new ObjectMapper();
		HighlightSearchResult[] results = null;
		try
		{
			results = mapper.readValue(json, HighlightSearchResult[].class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return results != null ? results : new HighlightSearchResult[0];
	}
}

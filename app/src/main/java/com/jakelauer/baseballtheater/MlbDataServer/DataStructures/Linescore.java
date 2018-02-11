package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jake on 1/14/2017.
 */

@Root(name = "linescore", strict = false)
public class Linescore implements Serializable
{
	@ElementList(name = "inning", inline = true, required = false)
	public List<LinescoreInning> linescoreInnings;

	@Element(name = "r")
	public Runs runs;

	@Element(name = "h")
	public Hits hits;

	@Element(name = "e")
	public Errors errors;

	public int getStartingInning()
	{
		int startingInning = 0;
		int inningCount = 9;
		if (linescoreInnings.size() > inningCount)
		{
			inningCount = linescoreInnings.size();
			startingInning = inningCount - 9;
		}

		return startingInning;
	}
}
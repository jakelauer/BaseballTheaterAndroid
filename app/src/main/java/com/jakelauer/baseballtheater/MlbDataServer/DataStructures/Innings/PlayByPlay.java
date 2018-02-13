package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.BoxScore.Player;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Jake on 2/8/2018.
 */

@Root(name = "game", strict = false)
public class PlayByPlay
{
	@Attribute
	public String atBat;

	@Attribute
	public String deck;

	@Attribute
	public String hole;

	@Attribute
	public String ind;

	@ElementList(name = "inning", inline = true, required = false)
	public List<Inning> innings;
}


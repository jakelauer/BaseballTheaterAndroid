package com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Innings;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class InningHalf
{
	@ElementList(name = "atbat", inline = true, required = false)
	public List<AtBat> atbat;
}

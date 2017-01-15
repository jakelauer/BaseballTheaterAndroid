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

@Root(name = "linescore", strict = false)
public class Linescore implements Serializable {
	@ElementList(name= "inning", inline = true, required = false)
	public List<Inning> innings;

	@Element(name = "r")
	public Runs runs;

	@Element(name = "h")
	public Hits hits;

	@Element(name = "e")
	public Errors errors;
}

@Root(strict = false)
class Inning extends HomeAway{

}

@Root(strict = false)
class Runs extends HomeAway{

}

@Root(strict = false)
class Hits extends HomeAway{

}

@Root(strict = false)
class Errors extends HomeAway{

}

@Root(strict = false)
class HomeAway implements Serializable
{
	@Attribute(name = "home", required = false)
	public String home;

	@Attribute(name = "away", required = false)
	public String away;
}
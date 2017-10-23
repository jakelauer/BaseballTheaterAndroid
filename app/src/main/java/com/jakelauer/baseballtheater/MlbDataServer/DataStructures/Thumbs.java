package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "thumbnails")
public class Thumbs
{
	@ElementList(entry = "thumb", inline = true)
	public List<String> thumbs;
}

package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "thumb", strict = false)
public class Thumb
{
	@Text
	public String text;
}
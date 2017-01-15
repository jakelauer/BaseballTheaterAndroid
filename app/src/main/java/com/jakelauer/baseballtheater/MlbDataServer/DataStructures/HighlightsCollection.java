package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jake on 1/14/2017.
 */

@Root(name = "highlights", strict = false)
public class HighlightsCollection  implements Serializable {
	@ElementList(name = "media")
	public List<Highlight> highlights;
}

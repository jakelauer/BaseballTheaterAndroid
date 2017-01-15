package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jake on 1/12/2017.
 */

@Root(name = "games", strict = false)
public class GameSummaryCollection implements Serializable {
    @ElementList(name="game", inline = true)
    public List<GameSummary> GameSummaries;
}

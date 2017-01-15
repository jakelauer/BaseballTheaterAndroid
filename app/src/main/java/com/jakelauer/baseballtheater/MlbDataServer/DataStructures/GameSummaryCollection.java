package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import com.jakelauer.baseballtheater.MlbDataServer.GameSummary;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Jake on 1/12/2017.
 */

@Root(name = "games", strict = false)
public class GameSummaryCollection {
    @Element(name="game", required = false)
    public List<GameSummary> GameSummaries;
}

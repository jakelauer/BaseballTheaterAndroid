package com.jakelauer.baseballtheater.MlbDataServer;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameCenter;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.DownloadListener;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.JsonLoader;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.XmlLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 1/14/2017.
 */

public class PatreonDataCreator
{
    private static final String url = "https://baseball.theater/Data/Patreon";

    public void get(final DownloadListener<JSONObject> downloadListener)
    {
        JsonLoader jsonLoader = new JsonLoader();
        jsonLoader.GetJson(url, downloadListener);
    }
}
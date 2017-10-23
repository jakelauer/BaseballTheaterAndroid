package com.jakelauer.baseballtheater.MlbDataServer;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.XmlLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jake on 1/12/2017.
 */

public class GameSummaryCreator
{

    private static final String UrlBase = "http://gd2.mlb.com";

    private String BuildUrl(DateTime date)
    {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("'year_'yyyy/'month_'MM/'day_'dd");
        String dateFolders = fmt.print(date);

        return GameSummaryCreator.UrlBase + "/components/game/mlb/" + dateFolders + "/master_scoreboard.xml";
    }

    public void GetSummaryCollection(DateTime date, ProgressListener<GameSummaryCollection> progressListener) throws IOException, ExecutionException, InterruptedException
    {
        String url = BuildUrl(date);

        XmlLoader<GameSummaryCollection> xmlLoader = new XmlLoader<>();
        xmlLoader.GetXml(url, progressListener, GameSummaryCollection.class);
    }
}

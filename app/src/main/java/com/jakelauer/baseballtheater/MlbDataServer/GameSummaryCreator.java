package com.jakelauer.baseballtheater.MlbDataServer;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummaryCollection;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.XmlLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jake on 1/12/2017.
 */

public class GameSummaryCreator {

    private static final String UrlBase = "http://gd2.mlb.com";

    private String BuildUrl(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'year_'yyyy/'month_'MM/'day_'dd");
        String dateFolders = simpleDateFormat.format(date);

        return GameSummaryCreator.UrlBase + "/components/game/mlb/" + dateFolders + "/master_scoreboard.xml";
    }

    public GameSummaryCollection GetSummaryCollection(Date date) throws IOException, ExecutionException, InterruptedException {
        String url = BuildUrl(date);
        GameSummaryCollection gameSummaryCollection = null;

        XmlLoader<GameSummaryCollection> xmlLoader = new XmlLoader();
        gameSummaryCollection = xmlLoader.GetXml(url, GameSummaryCollection.class);

        return gameSummaryCollection;
    }
}

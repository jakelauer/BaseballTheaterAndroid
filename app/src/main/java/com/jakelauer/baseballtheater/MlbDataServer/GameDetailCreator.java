package com.jakelauer.baseballtheater.MlbDataServer;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameCenter;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.XmlLoader;

/**
 * Created by Jake on 1/14/2017.
 */

public class GameDetailCreator {
    private static final String UrlBase = "http://gd2.mlb.com";

    private String directoryUrl;
    private String highlightsXmlUrl;
    private String gameCenterXmlUrl;
    private String gameSummaryXmlUrl;
	private String url;
	private Class<Object> classType;


	public GameDetailCreator(String directory, Boolean directoryIsFullyQualified) {
        this.directoryUrl = directoryIsFullyQualified
                ? directory
                : UrlBase + directory;

        this.highlightsXmlUrl = this.directoryUrl + "/media/mobile.xml";
        this.gameCenterXmlUrl = this.directoryUrl + "/gamecenter.xml";
        this.gameSummaryXmlUrl = this.directoryUrl + "/linescore.xml";
    }

    public void getHighlights(ProgressActivity progressActivity){
        getDetailItem(this.highlightsXmlUrl, progressActivity, HighlightsCollection.class);
    }

    public void getGameCenter(ProgressActivity progressActivity){
        getDetailItem(this.gameCenterXmlUrl, progressActivity, GameCenter.class);
    }

    public void getGameSummary(ProgressActivity progressActivity){
        getDetailItem(this.gameSummaryXmlUrl, progressActivity, GameSummary.class);
    }

    private <T> void getDetailItem(String url, ProgressActivity progressActivity, Class<T> classType){
		XmlLoader<T> xmlLoader = new XmlLoader();
        xmlLoader.GetXml(url, progressActivity, classType);
    }
}
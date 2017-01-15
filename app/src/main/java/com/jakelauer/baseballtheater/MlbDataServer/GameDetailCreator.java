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

    public HighlightsCollection getHighlights(){
        return this.getDetailItem(this.highlightsXmlUrl, HighlightsCollection.class);
    }

    public GameCenter getGameCenter(){
        return this.getDetailItem(this.gameCenterXmlUrl, GameCenter.class);
    }

    public GameSummary getGameSummary(){
        return this.getDetailItem(this.gameSummaryXmlUrl, GameSummary.class);
    }

    private <T> T getDetailItem(String url, Class<T> classType){
		XmlLoader<T> xmlLoader = new XmlLoader();
        T thing = xmlLoader.GetXml(url, classType);

        return thing;
    }
}
package com.jakelauer.baseballtheater.MlbDataServer;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameCenter;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.Utils.XmlLoader;

import java.util.ArrayList;
import java.util.List;

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

    public void getHighlights(final ProgressActivity progressActivity){
        ProgressActivity baseProgressActivity = new ProgressActivity() {
            @Override
            public void onProgressUpdate(double progress) {
                progressActivity.onProgressUpdate(progress);
            }

            @Override
            public void onProgressFinished(Object objectInstance) {
                HighlightsCollection hc = (HighlightsCollection) objectInstance;

                if(hc != null && hc.highlights != null){
                    for(Highlight highlight : hc.highlights) {
                        for(String url : highlight.urls) {
                            if (url.contains("1200K")) {
                                List<String> urls = new ArrayList<>();

								urls.add(url);

                                String url2500 = url.replace("1200K", "2500K");
                                String url1800 = url.replace("1200K", "1800K");

                                if(!urls.contains(url1800)) {
                                    urls.add(url1800);
                                }

                                if(!urls.contains(url2500)) {
                                    urls.add(url2500);
                                }

                                highlight.urls = urls;
								break;
                            }
                        }
                    }
                }

                progressActivity.onProgressFinished(hc);
            }
        };

        getDetailItem(this.highlightsXmlUrl, baseProgressActivity, HighlightsCollection.class);
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
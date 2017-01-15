package com.jakelauer.baseballtheater.MlbDataServer;

/**
 * Created by Jake on 1/12/2017.
 */

public class XmlParams
{
    String url;
    Class classType;

    public XmlParams(String url, Class classType){
        this.url = url;
        this.classType = classType;
    }
}

package com.philips.platform.appinfra.abtestclient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 310243577 on 10/6/2016.
 */

public class CacheModel {

    private HashMap<String, ArrayList<String>> testValues;

    private String appVersion;


    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    public HashMap<String, ArrayList<String>> getTestValues() {
        return testValues;
    }

    public void setTestValues(HashMap<String, ArrayList<String>> testValues) {
        this.testValues = testValues;
    }

}

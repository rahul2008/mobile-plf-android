/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 310243577 on 10/6/2016.
 */

class CacheModel {

    private HashMap<String, ArrayList<String>> mTestValues;

    private String mAppVersion;


    public String getAppVersion() {
        return mAppVersion;
    }

    public void setAppVersion(String appVersion) {
        this.mAppVersion = appVersion;
    }


    public HashMap<String, ArrayList<String>> getTestValues() {
        return mTestValues;
    }

    public void setTestValues(HashMap<String, ArrayList<String>> testValues) {
        this.mTestValues = testValues;
    }

}

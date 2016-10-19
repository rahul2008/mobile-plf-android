/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import java.util.HashMap;

/**
 * Created by 310243577 on 10/6/2016.
 */

class CacheModel {

    private HashMap<String, ValueModel> mTestValues;

    private String mAppVersion;


    public String getAppVersion() {
        return mAppVersion;
    }

    public void setAppVersion(String appVersion) {
        this.mAppVersion = appVersion;
    }


    public HashMap<String, ValueModel> getTestValues() {
        return mTestValues;
    }

    public void setTestValues(HashMap<String, ValueModel> testValues) {
        this.mTestValues = testValues;
    }

    static class ValueModel {

        private String testValue;
        private String updateType;


        public String getTestValue() {
            return testValue;
        }

        public void setTestValue(String testValue) {
            this.testValue = testValue;
        }

        public String getUpdateType() {
            return updateType;
        }

        public void setUpdateType(String updateType) {
            this.updateType = updateType;
        }
    }
}


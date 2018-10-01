/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import java.util.Map;

/**
 * The Cache Model for ABTest
 */

public class CacheModel {

    private Map<String, ValueModel> mTestValues;
    public Map<String, ValueModel> getTestValues() {
        return mTestValues;
    }
    public void setTestValues(Map<String, ValueModel> testValues) {
        this.mTestValues = testValues;
    }

    public static class ValueModel {

        private String testValue;
        private String updateType;
        private String appVersion;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

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


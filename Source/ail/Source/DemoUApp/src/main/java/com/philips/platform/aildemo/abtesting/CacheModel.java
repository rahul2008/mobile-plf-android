/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemo.abtesting;

import java.util.Map;

/**
 * The Cache Model for ABTest
 */

class CacheModel {

    private Map<String, ValueModel> mTestValues;
    private ValueModel valueModel;
    public Map<String, ValueModel> getTestValues() {
        return mTestValues;
    }

    public void setTestValues(Map<String, ValueModel> testValues) {
        this.mTestValues = testValues;
    }

    static class ValueModel {

        private String testValue;
        private String updateType;
        private int appVersion;

        public int getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(int appVersion) {
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


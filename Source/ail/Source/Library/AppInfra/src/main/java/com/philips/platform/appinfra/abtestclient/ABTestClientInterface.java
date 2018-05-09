/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import java.io.Serializable;
import java.util.Map;

/**
 * The ABTest client Interface
 */

public interface ABTestClientInterface extends Serializable {
    /**
     * NO_TESTS_DEFINED: no list of tests is configured
     * NO_CACHED_EXPERIENCES: tests are configured but no experience values are in cache
     * EXPERIENCES_NOT_UPDATED: tests are configured, cached experience values are of previous app start
     * EXPERIENCES_PARTIALLY_UPDATED: tests are configured, some experience values are updated from the server in this app start, others may still be from previous execution or not cached at all
     * EXPERIENCES_UPDATED: tests are configured, all experience values have been downloaded from the server for this app start
     */
    enum CACHESTATUSVALUES {
        NO_TESTS_DEFINED, NO_CACHED_EXPERIENCES, EXPERIENCES_NOT_UPDATED,
        EXPERIENCES_PARTIALLY_UPDATED, EXPERIENCES_UPDATED
    }

    /**
     * Update cache after app restart or update
     */
    enum UPDATETYPES {
        EVERY_APP_START(1), ONLY_AT_APP_UPDATE(2);

        private int value;


        UPDATETYPES(int i) {
            this.value = i;
        }

        public int getValue(){
            return value;
        }
    }

    /**
     *These tests will be refreshes if you update cache after app restart or update
     * @since 1.0.0
     */
    interface OnRefreshListener {
        enum ERRORVALUES {NO_NETWORK, EXPERIENCES_PARTIALLY_DOWNLOADED}


        void onSuccess();

        void onError(ERRORVALUES error, String message);
    }

    /**
     * Returns the status of the cached experiences for the configured list of tests.
     * At initialization of the module, the status is either NO_TESTS_DEFINED, NO_CACHED_EXPERIENCES,
     * or EXPERIENCES_NOT_UPDATED.
     * An updateCache() may change the state to EXPERIENCES_PARTIALLY_UPDATED or EXPERIENCES_UPDATED.
     *
     * @return status of the experience cache.
     * @since 1.0.0
     */
    CACHESTATUSVALUES getCacheStatus();

    /**
     * Provides value for a given test. If no value is available in cache,
     * the default value will be returned and stored in cache.
     *
     * @param requestNameKey     name of the test for which the value is to be provided
     * @param defaultValue value to use if no cached value is available
     * @return experience value for the requested test.
     * @since 1.0.0
     */
    String getTestValue(String requestNameKey, String defaultValue, UPDATETYPES updateType,
                        Map<String, Object> parameters);

    /**
     * Download experience values from the server. Call will have no effect if state equals
     * NO_TESTS_DEFINED or EXPERIENCES_UPDATED.
     *
     * @param listener for OnRefresh
     * @since 1.0.0
     */
    void updateCache(OnRefreshListener listener);
}
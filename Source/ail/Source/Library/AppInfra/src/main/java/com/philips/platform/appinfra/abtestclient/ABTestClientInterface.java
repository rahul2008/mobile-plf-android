/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Map;

/**
 * The ABTest client Interface
 */

public interface ABTestClientInterface extends Serializable {
    /**
     * EXPERIENCES_NOT_UPDATED: tests are configured, cached experience values are of previous app start
     * EXPERIENCES_UPDATED: tests are configured, all experience values have been downloaded from the server for this app start
     */
    enum CACHESTATUSVALUES {
        EXPERIENCE_NOT_UPDATED, EXPERIENCE_UPDATED
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
        enum ERRORVALUES {NO_NETWORK, SERVER_ERROR}


        void onSuccess();

        void onError(ERRORVALUES error);
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
    String getTestValue(@NonNull String requestNameKey, @NonNull String defaultValue, UPDATETYPES updateType);

    /**
     * Download experience values from the server. Call will have no effect if state equals
     * NO_TESTS_DEFINED or EXPERIENCES_UPDATED.
     *
     * @param listener for OnRefresh
     * @since 1.0.0
     */
    void updateCache(OnRefreshListener listener);

    /**
     * API to enable developer mode to true or false
     * @param state - pass true to enable or false to disable
     * @since 2018.4.0
     */
    void enableDeveloperMode(boolean state);
}
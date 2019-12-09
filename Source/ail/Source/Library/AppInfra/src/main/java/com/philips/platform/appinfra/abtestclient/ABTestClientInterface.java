/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.abtestclient;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * The ABTest client Interface
 */

public interface ABTestClientInterface extends Serializable {
    /**
     * EXPERIENCES_NOT_UPDATED: tests are configured, cached experience values are of previous app start
     * EXPERIENCES_UPDATED: tests are configured, all experience values have been downloaded from the server for this app start
     * @since 2018.4.0
     */
    enum CACHESTATUS {
        EXPERIENCE_NOT_UPDATED, EXPERIENCE_UPDATED
    }

    /**
     * Update cache after app restart or update
     * @since 2018.4.0
     */
    enum UPDATETYPE {
        APP_RESTART(1), APP_UPDATE(2);

        private int value;


        UPDATETYPE(int i) {
            this.value = i;
        }

        public int getValue(){
            return value;
        }
    }

    /**
     *These tests will be refreshes if you update cache after app restart or update
     * @since 2018.4.0
     */
    interface OnRefreshListener {
        enum ERRORVALUE {NO_NETWORK, SERVER_ERROR}


        void onSuccess();

        void onError(ERRORVALUE error);
    }

    /**
     * Returns the status of the cached experiences for the configured list of tests.
     * At initialization of the module, the status is EXPERIENCES_NOT_UPDATED.
     * On successful updateCache() state will be updated to EXPERIENCES_UPDATED.
     *
     * @return status of the experience cache.
     * @since 2018.4.0
     */
    CACHESTATUS getCacheStatus();

    /**
     * Provides value for a given test. If no value is available in cache,
     * the default value will be returned and stored in cache.
     *
     * @param requestNameKey     name of the test for which the value is to be provided
     * @param defaultValue value to use if no cached value is available
     * @return experience value for the requested test.
     * @since 2018.4.0
     */
    String getTestValue(@NonNull String requestNameKey, @NonNull String defaultValue, UPDATETYPE updateType);

    /**
     * Download updated experience values from the server. Call will have no effect if state equals
     * EXPERIENCES_NOT_UPDATED or EXPERIENCES_UPDATED.
     *
     * @param listener for OnRefresh
     * @since 2018.4.0
     */
    void updateCache(OnRefreshListener listener);

    /**
     * API to enable developer mode to true or false
     * @param state - pass true to enable or false to disable
     * @since 2018.4.0
     */
    void enableDeveloperMode(boolean state);


    /**
     *
     * @return This method returns the A/B-testing consent identifier
     * @since 2018.4.0
     */
    String getAbTestingConsentIdentifier();

    /**
     * API to tag A/B testing events
     * @param eventName - name of the event to be tagged
     * @param params - parameters to be passed if required
     * @since 2018.5.0
     */
    void tagEvent(String eventName, Bundle params);
}
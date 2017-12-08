/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core;

/**
 * The Global Error Handling Interface for notifying the sync errors
 */
public interface ErrorHandlingInterface {
    /**
     * Notify the Propositions that error occurred during sync
     *
     * @param error The HTTP error code
     */
    void syncError(int error);

    /**
     * Notify the Propositions that error occurred while using service discovery for fetching the URL
     *
     * @param error Error returned from Service Discovery is passed to Propositions (App-Infra Component)
     */
    void onServiceDiscoveryError(String error);
}

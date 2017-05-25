/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

/**
 * Interface Callback for notifying Synchronization Status
 */
public interface SynchronisationChangeListener {
    /**
     * CallBack for notifying Data-Pull Success
     */
    void dataPullSuccess();

    /**
     * CallBack for notifying Data-Push Success
     */
    void dataPushSuccess();

    /**
     * Callback for notifying Data-Pull Failure
     * @param e Exception (Error) returned from Retrofit response
     */
    void dataPullFail(Exception e);

    /**
     * Callback for notifying Data-Push Failure
     * @param e Exception (Error) returned from Retrofit response
     */
    void dataPushFail(Exception e);

    /**
     * Callback for notifying a sync cycle complete
     */
    void dataSyncComplete();
}

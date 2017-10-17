/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

/**
 * Interface Callback for notifying Synchronization Completion status
 */
public interface SynchronisationCompleteListener {
    /**
     * CallBack for notifying sync complete
     */
    void onSyncComplete();

    /**
     * CallBack for notifying sync failed
     * @param exception Exception (Error) returned from Retrofit response while sync failed
     */
    void onSyncFailed(Exception exception);
}

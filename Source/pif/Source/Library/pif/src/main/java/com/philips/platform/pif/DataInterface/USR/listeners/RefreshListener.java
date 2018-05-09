/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR.listeners;

/**
 * Interface to refresh listeners
 * @since 2018.1.0
 */
public interface RefreshListener {

    /**
     * {@code onRefreshSessionSuccess} method is invoked on successful user refresh
     * @since 2018.1.0
     */
    void onRefreshSessionSuccess();

    /**
     * {@code onRefreshSessionFailure} method is invoked on user refresh Failure
     * @param error error code on failure
     * @since 2018.1.0
     */
    void onRefreshSessionFailure(int error);

    /**
     * {@code onRefreshSessionInProgress} method is invoked on refresh login session in progress
     * @param message  progress message on refresh login session
     * @since 2018.1.0
     */
    void onRefreshSessionInProgress(String message);

    /**
     * method is invoked on user refresh Failure due to excess login at multiple devices and gets forced logged out on refresh session
     * @since 2018.1.0
     */
    void onForcedLogout();

}

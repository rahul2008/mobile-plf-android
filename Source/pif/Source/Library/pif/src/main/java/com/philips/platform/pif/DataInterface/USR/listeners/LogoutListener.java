/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR.listeners;

/**
 * Interface to handle Logout
 * @since 2018.1.0
 */
public interface LogoutListener {

    /**
     * {@code onLogoutSuccess} method is invoked on Logout Success
     * @since 2018.1.0
     */
    void onLogoutSuccess();

    /**
     * {@code onLogoutFailure} method is invoked on Logout Failure
     * @param errorCode error code on failure
     * @param errorMessage error message on failure
     * @since 2018.1.0
     */
    void onLogoutFailure(int errorCode, String errorMessage);

}

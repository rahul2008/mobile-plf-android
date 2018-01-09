/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to handle Logout
 * @since 1.0.0
 */
public interface LogoutHandler {

    /**
     * {@code onLogoutSuccess} method to validate on on Logout Success
     * @since 1.0.0
     */
    void onLogoutSuccess();

    /**
     * {@code onLogoutFailure} method to validate on Logout Failure
     * @param responseCode  failure response code in integer
     * @param message  failure message
     * @since 1.0.0
     */
    void onLogoutFailure(int responseCode, String message);
}

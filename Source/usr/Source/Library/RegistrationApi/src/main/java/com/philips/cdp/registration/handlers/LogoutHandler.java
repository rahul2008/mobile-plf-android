/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * Logout handler interface
 */
public interface LogoutHandler {

    /**
     * {@code onLogoutSuccess} method to validate on on Logout Success
     */
    void onLogoutSuccess();

    /**
     * {@code onLogoutFailure} method to validate on Logout Failure
     * @param responseCode
     * @param message
     */
    void onLogoutFailure(int responseCode, String message);
}

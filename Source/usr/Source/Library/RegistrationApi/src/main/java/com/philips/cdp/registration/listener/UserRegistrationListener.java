
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.listener;

/**
 * USR Registration callback.
 */
public interface UserRegistrationListener {
    /**
     * Callback when user is logged out
     */
    void onUserLogoutSuccess();

    /**
     * Callback when user is failed to log out
     */
    void onUserLogoutFailure();

    /**
     * Callback when logout happned due to invalid access token.
     */
    void onUserLogoutSuccessWithInvalidAccessToken();
}

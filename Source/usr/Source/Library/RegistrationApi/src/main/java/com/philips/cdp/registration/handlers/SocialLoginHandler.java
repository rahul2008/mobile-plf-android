/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

/**
 *
 * It is a callback class to proposition for handling Social login
 * @since 1.0.0
 */
public interface SocialLoginHandler {

    /**
     * {@code onLoginSuccess} method is invoked on login success
     * @since 1.0.0
     */
    void onLoginSuccess();

    /**
     * {@code userRegistrationFailureInfo} method is invoked on login fails with error
     * @param userRegistrationFailureInfo  gives the failure information when social login fails
     * @since 1.0.0
     */
    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);
}

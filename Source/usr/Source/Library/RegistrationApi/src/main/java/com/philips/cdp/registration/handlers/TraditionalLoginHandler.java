
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
 * It is used to handle traditional login
 * @since 1.0.0
 */
public interface TraditionalLoginHandler {

    /**
     * {@code onLoginSuccess}method to on login success
     * @since 1.0.0
     */
    void onLoginSuccess();

    /**
     * {@code onLoginFailedWithError }method to on login failed with error
     * @param userRegistrationFailureInfo gives user registration failure information when philips login fails
     * @since 1.0.0
     */
    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);

}

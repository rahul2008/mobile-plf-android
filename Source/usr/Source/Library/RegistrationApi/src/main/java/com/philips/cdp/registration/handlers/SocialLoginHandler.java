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
 * Created by 310190722 on 12/23/2015.
 * Social login handler interface
 */
public interface SocialLoginHandler {

    /**
     * {@code onLoginSuccess} method to on login success
     */
    void onLoginSuccess();

    /**
     * {@code userRegistrationFailureInfo}mehtod to on login faled with error
     * @param userRegistrationFailureInfo user registration failure info
     */
    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);
}

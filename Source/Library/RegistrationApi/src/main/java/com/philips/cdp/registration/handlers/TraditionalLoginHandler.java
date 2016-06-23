
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
 * Traditional login handler interface
 */
public interface TraditionalLoginHandler {

    /**
     * {@code onLoginSuccess}method to on login success
     */
    void onLoginSuccess();

    /**
     * {@code onLoginFailedWithError }method to on login failed with error
     * @param userRegistrationFailureInfo user registration failure info
     */
    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);

}

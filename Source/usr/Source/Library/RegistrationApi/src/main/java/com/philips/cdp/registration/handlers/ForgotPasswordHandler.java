
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
 * Forgot password handler interface
 */
public interface ForgotPasswordHandler {

    /**
     * {@code onSendForgotPasswordSuccess} method to validate on send forgot password success
     */
    void onSendForgotPasswordSuccess();

    /**
     *{@code onSendForgotPasswordFailedWithError} method to validate on on send forgot password failed with error
     * @param userRegistrationFailureInfo
     */
    void onSendForgotPasswordFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}

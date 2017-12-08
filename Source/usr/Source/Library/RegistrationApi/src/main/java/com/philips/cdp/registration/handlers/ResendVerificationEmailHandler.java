
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
 * Resend verification email handler interface
 */
public interface ResendVerificationEmailHandler {
    /**
     *{@code onResendVerificationEmailSuccess} method to on resend verification email success
     */
    void onResendVerificationEmailSuccess();

    /**
     * {@code onResendVerificationEmailFailedWithError} method to on resend verification email failed with error
     * @param userRegistrationFailureInfo user registration failure info
     */
    void onResendVerificationEmailFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}

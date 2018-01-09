
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
 * It is used to handle resend verification
 * @since 1.0.0
 */
public interface ResendVerificationEmailHandler {
    /**
     *{@code onResendVerificationEmailSuccess} method to on resend verification email success
     * @since 1.0.0
     */
    void onResendVerificationEmailSuccess();

    /**
     * {@code onResendVerificationEmailFailedWithError} method to on resend verification email failed with error
     * @param userRegistrationFailureInfo   gives the failure information when resend verification email fails
     * @since 1.0.0
     */
    void onResendVerificationEmailFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}

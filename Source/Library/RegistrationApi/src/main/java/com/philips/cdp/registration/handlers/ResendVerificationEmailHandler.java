
package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

public interface ResendVerificationEmailHandler {

    void onResendVerificationEmailSuccess();

    void onResendVerificationEmailFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}

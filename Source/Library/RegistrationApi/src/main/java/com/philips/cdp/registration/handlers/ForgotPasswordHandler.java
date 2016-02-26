
package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

public interface ForgotPasswordHandler {

    void onSendForgotPasswordSuccess();

    void onSendForgotPasswordFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}

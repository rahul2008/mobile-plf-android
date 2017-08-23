package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.dao.*;

/**
 * Created by philips on 22/06/17.
 */

public interface ForgotPasswordContract {
    void handleUiState(boolean isOnline);
    void handleUiStatus();
    void handleSendForgotPasswordSuccess();
    void handleSendForgotPasswordFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);
    void hideForgotPasswordSpinner();
    void forgotPasswordErrorMessage(String errorMsgId);
    void trackAction(String state, String key, String value);
}

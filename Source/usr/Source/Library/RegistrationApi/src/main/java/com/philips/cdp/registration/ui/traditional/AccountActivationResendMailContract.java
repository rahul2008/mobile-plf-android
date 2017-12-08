package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.dao.*;

public interface AccountActivationResendMailContract {
    void handleUiState(boolean isOnline);

    void handleResendVerificationEmailSuccess();

    void handleResendVerificationEmailFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}

package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.dao.*;

/**
 * Created by philips on 22/06/17.
 */

public interface AccountActivationResendMailContract {
    void handleUiState(boolean isOnline);
    void handleResendVerificationEmailSuccess();
    void handleResendVerificationEmailFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo);

}

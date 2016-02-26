
package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

public interface TraditionalLoginHandler {

    void onLoginSuccess();

    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);

}

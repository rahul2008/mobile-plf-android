package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

/**
 * Created by 310190722 on 12/23/2015.
 */
public interface SocialLoginHandler {

    void onLoginSuccess();

    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);
}

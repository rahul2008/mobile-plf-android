
package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

public interface TraditionalLoginHandler {

	public void onLoginSuccess();

	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);
}

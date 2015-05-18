
package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;

public interface TraditionalLoginHandler {

	public void onLoginSuccess();

	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);
}

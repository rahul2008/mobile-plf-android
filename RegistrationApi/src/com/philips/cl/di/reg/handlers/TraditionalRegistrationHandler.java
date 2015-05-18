
package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;

public interface TraditionalRegistrationHandler {

	public void onRegisterSuccess();

	public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo);
}

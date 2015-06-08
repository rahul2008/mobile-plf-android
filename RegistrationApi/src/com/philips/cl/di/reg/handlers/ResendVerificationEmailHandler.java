
package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;

public interface ResendVerificationEmailHandler {

	public void onResendVerificationEmailSuccess();

	public void onResendVerificationEmailFailedWithError(
	        UserRegistrationFailureInfo userRegistrationFailureInfo);
}

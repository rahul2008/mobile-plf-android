
package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

public interface ResendVerificationEmailHandler {

	public void onResendVerificationEmailSuccess();

	public void onResendVerificationEmailFailedWithError(
	        UserRegistrationFailureInfo userRegistrationFailureInfo);
}


package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;

public interface ForgotPasswordHandler {

	public void onSendForgotPasswordSuccess();

	public void onSendForgotPasswordFailedWithError(
	        UserRegistrationFailureInfo userRegistrationFailureInfo);
}

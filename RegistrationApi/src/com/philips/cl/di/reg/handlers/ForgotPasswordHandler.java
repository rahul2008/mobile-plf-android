package com.philips.cl.di.reg.handlers;

import com.philips.cl.di.reg.dao.ForgotPasswordFailureInfo;

public interface ForgotPasswordHandler {
	public void onSendForgotPasswordSuccess();
	public void onSendForgotPasswordFailedWithError(ForgotPasswordFailureInfo forgotPasswordFailureInfo);
}

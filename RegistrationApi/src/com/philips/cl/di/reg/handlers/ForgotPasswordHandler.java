package com.philips.cl.di.reg.handlers;

public interface ForgotPasswordHandler {
	public void onSendForgotPasswordSuccess();
	public void onSendForgotPasswordFailedWithError(int error);
}

package com.philips.cl.di.reg.handlers;

public interface ResendVerificationEmailHandler {
	public void onResendVerificationEmailSuccess();
	public void onResendVerificationEmailFailedWithError(int error);
}

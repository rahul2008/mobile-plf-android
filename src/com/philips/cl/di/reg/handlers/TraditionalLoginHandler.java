package com.philips.cl.di.reg.handlers;

public interface TraditionalLoginHandler {
	public void onLoginSuccess();
	public void onLoginFailedWithError(int error);
}

package com.philips.cl.di.reg.handlers;

public interface TraditionalRegistrationHandler {
	public void onRegisterSuccess();
	public void onRegisterFailedWithFailure(int error);
}

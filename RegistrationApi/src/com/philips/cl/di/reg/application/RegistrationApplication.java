package com.philips.cl.di.reg.application;

import android.app.Application;

public class RegistrationApplication extends Application {

	private static RegistrationApplication registrationInstance;

	private RegistrationApplication() {

	}

	public static RegistrationApplication getInstance() {

		if (registrationInstance == null) {
			registrationInstance = new RegistrationApplication();
		}
		return registrationInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}

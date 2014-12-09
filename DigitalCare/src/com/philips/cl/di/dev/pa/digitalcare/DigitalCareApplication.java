package com.philips.cl.di.dev.pa.digitalcare;

import android.app.Application;

import com.philips.cl.di.dev.pa.digitalcare.util.FragmentObserver;

/*
 *	DigitalCareApplication is Application class for DigitalCare app.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2014
 */
public class DigitalCareApplication extends Application {

	private static DigitalCareApplication mDigitalCareApplication = null;
	private FragmentObserver mFragmentObserver = null;

	@Override
	public void onCreate() {
		super.onCreate();
		setApplication(this);
		mFragmentObserver = new FragmentObserver();
	}

	private static void setApplication(Application application) {
		mDigitalCareApplication = (DigitalCareApplication) application;
	}

	public static DigitalCareApplication getAppContext() {
		return mDigitalCareApplication;
	}

	public FragmentObserver getObserver() {
		return mFragmentObserver;
	}
}

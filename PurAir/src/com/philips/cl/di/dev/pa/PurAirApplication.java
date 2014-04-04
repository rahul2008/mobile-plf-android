package com.philips.cl.di.dev.pa;
import android.app.Application;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		setApplication(this);
	}
	
	private static void setApplication(Application application) {
		mInstance = (PurAirApplication) application;
	}
	
	public static PurAirApplication getAppContext() {
		return mInstance;
	}
}

package com.philips.cl.di.dev.pa.pureairui;
import android.app.Application;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	
	@Override
	public void onCreate() {
//		mInstance = (PurAirApplication) getApplicationContext();
		setApplication(this);
		
		super.onCreate();
	}
	
	private static void setApplication(Application application) {
		mInstance = (PurAirApplication) application;
	}
	
	public static PurAirApplication getAppContext() {
		return mInstance;
	}
}

package com.philips.cl.di.dev.pa.pureairui;
import android.app.Application;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	
	@Override
	public void onCreate() {
		mInstance = (PurAirApplication) getApplicationContext();
		super.onCreate();
	}
	
	public static PurAirApplication getAppContext() {
		return mInstance;
	}
}

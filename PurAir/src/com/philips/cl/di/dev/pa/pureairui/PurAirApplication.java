package com.philips.cl.di.dev.pa.pureairui;
import android.app.Application;
import android.content.Context;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	
	@Override
	public void onCreate() {
		mInstance = this;
		super.onCreate();
	}
	
	public static PurAirApplication getAppContext() {
		return mInstance;
	}
}

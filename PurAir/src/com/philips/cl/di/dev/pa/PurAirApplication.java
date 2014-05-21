package com.philips.cl.di.dev.pa;
import com.philips.cl.di.dev.pa.util.ALog;

import android.app.Application;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	private static boolean isDemoModeEnable;
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        	ALog.i(ALog.TEMP, e.getMessage());
        }
		setApplication(this);
	}
	
	private static void setApplication(Application application) {
		mInstance = (PurAirApplication) application;
	}
	
	public static PurAirApplication getAppContext() {
		return mInstance;
	}
	
	public static boolean isDemoModeEnable() {
		return isDemoModeEnable;
	}

	public static void setDemoModeEnable(boolean isDemoMode) {
		isDemoModeEnable = isDemoMode;
	}
}

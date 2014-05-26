package com.philips.cl.di.dev.pa;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.util.ALog;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	private static boolean isDemoModeEnable;
	
	private NotificationRegisteringManager mNotificationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		ALog.i(ALog.APPLICATION, "New application start");
		/**
		 * Added this to stop the OS from destroying an AsyncTask.
		 * Please refer : https://code.google.com/p/android/issues/detail?id=20915
		 */
		try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        	ALog.i(ALog.TEMP, e.getMessage());
        }
		setApplication(this);
		
		// Ensure app is registered for notifications
		getNotificationRegisteringManager();
	}
	
	public static int getAppVersion() {
		try {
			PackageInfo packageInfo = getAppContext().getPackageManager().getPackageInfo(getAppContext().getPackageName(), 0);
			ALog.i(ALog.APPLICATION, "Application version: " + packageInfo.versionName + " (" + packageInfo.versionCode + ")");
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private static void setApplication(Application application) {
		mInstance = (PurAirApplication) application;
	}
	
	public static PurAirApplication getAppContext() {
		return mInstance;
	}
	
	public NotificationRegisteringManager getNotificationRegisteringManager() {
		if (mNotificationManager == null) {
			mNotificationManager = new NotificationRegisteringManager();
			mNotificationManager.registerAppForNotification();
		}
		return mNotificationManager;
	}
	
	public static boolean isDemoModeEnable() {
		return isDemoModeEnable;
	}

	public static void setDemoModeEnable(boolean isDemoMode) {
		if (isDemoMode) {
			ALog.i(ALog.APPLICATION, "Application is now in Demo Mode");
		} else {
			ALog.i(ALog.APPLICATION, "Application is now in Normal Mode");
		}
		isDemoModeEnable = isDemoMode;
	}
}

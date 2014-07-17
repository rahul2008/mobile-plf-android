package com.philips.cl.di.dev.pa;
import java.util.Locale;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;


public class PurAirApplication extends Application {
	
	private static PurAirApplication mInstance = null;
	
	private NotificationRegisteringManager mNotificationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		printCurrentLanguage();
		preventOSFromDestroyingAsyncTasks();
		configureUrlConnectionSocketReuse();
		toggleLogging();
		
		ALog.i(ALog.APPLICATION, "New application start");
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
		SharedPreferences pref = 
				getAppContext().getSharedPreferences(AppConstants.DEMO_MODE_PREF, Activity.MODE_PRIVATE);
		return pref.getBoolean(AppConstants.DEMO_MODE_ENABLE_KEY, false);
	}

	public static void setDemoModeEnable(boolean isDemoMode) {
		SharedPreferences pref = 
				getAppContext().getSharedPreferences(AppConstants.DEMO_MODE_PREF, Activity.MODE_PRIVATE);
		pref.edit().putBoolean(AppConstants.DEMO_MODE_ENABLE_KEY, isDemoMode).commit();
	}
	
	public static void setDemoModePurifier(String eui64) {
		SharedPreferences pref = 
				getAppContext().getSharedPreferences(AppConstants.DEMO_MODE_PREF, Activity.MODE_PRIVATE);
		pref.edit().putString(AppConstants.DEMO_MODE_PURIFIER_KEY, eui64).commit();
	}
	
	public static String getDemoModePurifierEUI64() {
		SharedPreferences pref = 
				getAppContext().getSharedPreferences(AppConstants.DEMO_MODE_PREF, Activity.MODE_PRIVATE);
		return pref.getString(AppConstants.DEMO_MODE_PURIFIER_KEY, "");
	}
	
	/**
	 * Bug 1943: Prevent Sockets from being reused.
	 * - Only 1 socket opened per Purifier at any given point in time
	 * - Each socket is only used once, and after it is closed
	 */
	private void configureUrlConnectionSocketReuse() {
		System.setProperty("http.keepAlive", "false");
		System.setProperty("http.maxConnections", "1");
		ALog.d(ALog.APPLICATION, "Keeping sockets alive: " + System.getProperty("http.keepAlive"));
		ALog.d(ALog.APPLICATION, "Max connections: " + System.getProperty("http.maxConnections"));
	}
	
	/**
	 * Added this to stop the OS from destroying an AsyncTask.
	 * Please refer : https://code.google.com/p/android/issues/detail?id=20915
	 */
	private void preventOSFromDestroyingAsyncTasks() {
		
		try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        	ALog.i(ALog.APPLICATION, e.getMessage());
        }
	}
	
	private boolean isDebugBuild() {
		return (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}
	
	private void toggleLogging() {
		if (isDebugBuild()) {
			ALog.enableLogging();
		} else {
			ALog.disableLogging();
		}
	}
	
	private void printCurrentLanguage() {
		ALog.i(ALog.APPLICATION, "Current language: " + LanguageUtils.getLanguageForLocale(Locale.getDefault()));
	}
}

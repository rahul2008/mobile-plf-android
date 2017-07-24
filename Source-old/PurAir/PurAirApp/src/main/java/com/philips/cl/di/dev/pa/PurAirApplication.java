package com.philips.cl.di.dev.pa;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cl.di.dev.pa.buyonline.ImageLoaderUtils;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.PurAirKPSConfiguration;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierFactory;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

import java.io.File;
import java.util.Locale;

public class PurAirApplication extends Application {

	private static PurAirApplication mInstance = null;
	public static final String CACHEDIR_IMG = Environment.getExternalStorageDirectory().getPath() + "/philips/air/imgs/";
	public Object tmpObj; //CO App

	@Override
	public void onCreate() {
		super.onCreate();
		MetricsTracker.initContext(this);
		printCurrentLanguage();
		preventOSFromDestroyingAsyncTasks();
		configureUrlConnectionSocketReuse();
		toggleLoggingAndTagging();
		initImageLoader();

		ALog.i(ALog.APPLICATION, "New application start");
		setApplication(this);

		PurifierDatabase applianceDatabase = new PurifierDatabase(this);
		applianceDatabase.triggerOnDatabaseUpdateIfNeeded();

		CppController.createSharedInstance(getAppContext(), new PurAirKPSConfiguration());
        DICommClientWrapper.initializeDICommLibrary(getApplicationContext(),new AirPurifierFactory(),applianceDatabase,CppController.getInstance());
	}

	private void initImageLoader() {
		ImageLoaderUtils.initImageLoader(getApplicationContext(),
				ImageLoaderUtils.getDisplayImageOptions(true,
						-1, -1,
						-1, -1, -1), new File(CACHEDIR_IMG), new Md5FileNameGenerator(),
						false);

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
        	ALog.i(ALog.APPLICATION, "Error: " + e.getMessage());
        }
	}

	private boolean isDebugBuild() {
		return (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}

	private void toggleLoggingAndTagging() {
		if (isDebugBuild()) {
			ALog.enableLogging();
            DICommLog.enableLogging();
//			MetricsTracker.disableTagging();
		} else {
			ALog.disableLogging();
            DICommLog.disableLogging();
			MetricsTracker.enableTagging();
		}
	}

	private void printCurrentLanguage() {
		ALog.i(ALog.APPLICATION, "Current language: " + LanguageUtils.getLanguageForLocale(Locale.getDefault()));
	}

}

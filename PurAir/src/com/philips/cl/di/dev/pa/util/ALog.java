package com.philips.cl.di.dev.pa.util;

import android.util.Log;

/**
 * Custom log class:
 * - Defines all log tags
 * - Intercepts logs so more processing is possible
 * 
 * @author jmols
 */
public class ALog {
	
	public static final String TEMP = "Temp"; // Use for temporary logs during development
	
	public static final String ACTIVITY = "ActivityLifecycle";
	public static final String FRAGMENT = "FragmentLifecycle";
	
	public static final String EWS = "EasyWifiSetup";
	public static final String WIFI = "WifiNetworks ";
	public static final String ICPCLIENT = "IcpClient";
	public static final String SECURITY = "DISecurity";
	public static final String SSDP = "Ssdp";
	public static final String KPS = "KPS";
	public static final String MAINACTIVITY = "MainActivityAir" ;
	public static final String DATABASE = "DatabaseAir" ;
	public static final String CPPCONTROLLER="CppController";

	public static final String INDOOR_DETAILS = "IndoorDetails";
	
	private static boolean isLoggingEnabled = true;
	
	public static void enableLogging() {
		isLoggingEnabled = true;
	}
	
	public static void disableLogging() {
		isLoggingEnabled = false;
	}

	public static void d(String TAG, String message) {
		if (isLoggingEnabled) {
			Log.d(TAG, message);
		}
	}
	
	public static void e(String TAG, String message) {
		if (isLoggingEnabled) {
			Log.e(TAG, message);
		}
	}
	
	public static void i(String TAG, String message) {
		if (isLoggingEnabled) {
			Log.i(TAG, message);
		}
	}
	
	public static void v(String TAG, String message) {
		if (isLoggingEnabled) {
			Log.v(TAG, message);
		}
	}
	
	public static void w(String TAG, String message) {
		if (isLoggingEnabled) {
			Log.w(TAG, message);
		}
	}
}

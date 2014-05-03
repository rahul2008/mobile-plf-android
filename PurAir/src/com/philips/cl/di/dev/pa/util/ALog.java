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
	public static final String OUTDOOR_DETAILS = "OutdoorDetails";
	public static final String INDOOR_RDCP = "IndoorRdcp";
	public static final String ANIMATOR_CONST = "AnimatorConstants";
	public static final String AIRPURIFIER_CONTROLER = "AirPurifierController";
	public static final String PAIRING="Pairing";
	public static final String SUBSCRIPTION = "Subscription" ;
	public static final String FIRMWARE = "Firmware";
	public static final String CONNECTIVITY = "Connectivity" ;
	public static final String PARSER = "DataParser" ;
	public static final String DIAGNOSTICS="Diagnostics";
	public static final String PURIFIER_MANAGER = "PurifierManager";
	public static final String DASHBOARD="Dashboard";
	public static final String OUTDOOR_LOCATION="OutdoorLocation";
	public static final String TASK_GET="TaskGetHttp";
	
	private static boolean isLoggingEnabled = true;
	
	public static void enableLogging() {
		isLoggingEnabled = true;
	}
	
	public static void disableLogging() {
		isLoggingEnabled = false;
	}

	public static void d(String tag, String message) {
		if (isLoggingEnabled) {
			Log.d(tag, message);
		}
	}
	
	public static void e(String tag, String message) {
		if (isLoggingEnabled) {
			Log.e(tag, message);
		}
	}
	
	public static void i(String tag, String message) {
		if (isLoggingEnabled) {
			Log.i(tag, message);
		}
	}
	
	public static void v(String tag, String message) {
		if (isLoggingEnabled) {
			Log.v(tag, message);
		}
	}
	
	public static void w(String tag, String message) {
		if (isLoggingEnabled) {
			Log.w(tag, message);
		}
	}
}

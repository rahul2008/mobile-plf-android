package com.philips.cl.di.dev.pa.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
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
	public static final String ERROR = "Error"; // Use to log errors
	public static final String APPLICATION = "PurAirApplication";
	public static final String ACTIVITY = "ActivityLifecycle";
	public static final String FRAGMENT = "FragmentLifecycle";
	public static final String EWS = "EasyWifiSetup";
	public static final String DEMO_MODE = "DemoMode";
	public static final String WIFI = "WifiNetworks ";
	public static final String ICPCLIENT = "IcpClient";
	public static final String SECURITY = "DISecurity";
	public static final String SSDP = "Ssdp";
	public static final String KPS = "KPS";
	public static final String MAINACTIVITY = "MainActivityAir" ;
	public static final String FILTER_STATUS_FRAGMENT = "FilterStatusFragment" ;
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
	public static final String SCHEDULER = "Scheduler";
	public static final String CONNECTIVITY = "Connectivity" ;
	public static final String PARSER = "DataParser" ;
	public static final String DIAGNOSTICS="Diagnostics";
	public static final String PURIFIER_MANAGER = "AirPurifierManager";
	public static final String DASHBOARD="Dashboard";
	public static final String OUTDOOR_LOCATION="OutdoorLocation";
	public static final String TASK_GET="TaskGetHttp";
	public static final String DISCOVERY = "DiscoveryManager";
	public static final String NETWORKMONITOR = "NetworkMonitor";
	public static final String UDP = "UDPSocket";
	public static final String NOTIFICATION="NotificationAir";
	public static final String USER_REGISTRATION = "UserRegistration";
	public static final String DEVICEHANDLER = "DeviceHandler";
	public static final String DRAWER = "Drawer";
	public static final String SSDPHELPER = "SsdpHelper";
	public static final String CPPDISCHELPER = "CppDiscoveryHelper";
	public static final String TOOLS = "ToolsFragment";
	public static final String APP_START_UP = "AppStartUp";
	public static final String MANAGE_PUR = "ManagePurifier";
	public static final String MARKER_ACTIVITY = "MarkerActivity";
	public static final String FILE_DOWNLOAD = "FileDownload";
	public static final String TAGGING = "Tagging";
	public static final String APPLIANCE = "Appliance";
	public static final String AIRPORT = "AirPort";
	public static final String DEVICEPORT = "DevicePort";
    public static final String WIFIPORT = "WifiPort";
    public static final String WIFIUIPORT = "WifiUIPort";
    public static final String PAIRINGPORT = "PairingPort";
	public static final String FIRMWAREPORT = "FirmwarePort";
	public static final String SCHEDULELISTPORT = "ScheduleListPort";
	public static final String LOCALREQUEST = "LocalRequest";
	public static final String REMOTEREQUEST = "RemoteRequest";
	public static final String REQUESTQUEUE = "RequestQueue";
	public static final String LOCAL_SUBSCRIPTION = "LocalSubscription" ;
	public static final String REMOTE_SUBSCRIPTION = "RemoteSubscription" ;
	public static final String APPLIANCE_MANAGER = "ApplianceManager" ;

	private static boolean isLoggingEnabled = true;

	private static boolean isSaveToFileEnabled = false;

	public static void initLoggingToFile() {
		if(!isSaveToFileEnabled) return;
		try {
			createFileOnDevice(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void enableLogging() {
		isLoggingEnabled = true;
	}

	public static void disableLogging() {
		isLoggingEnabled = false;
	}

	public static boolean isLoggingEnabled() {
		return isLoggingEnabled;
	}

	public static void d(String tag, String message) {
		if (isLoggingEnabled) {
			Log.d(tag, message);
			writeToFile(tag + " : "+message);
		}
	}

	public static void e(String tag, String message) {
		if (isLoggingEnabled) {
			Log.e(tag, message);
			writeToFile(tag + " : "+message);
		}
	}

	public static void i(String tag, String message) {
		if (isLoggingEnabled) {
			Log.i(tag, message);
			writeToFile(tag + " : "+message);
		}
	}

	public static void v(String tag, String message) {
		if (isLoggingEnabled) {
			Log.v(tag, message);
			writeToFile(tag + " : "+message);
		}
	}

	public static void w(String tag, String message) {
		if (isLoggingEnabled) {
			Log.w(tag, message);
			writeToFile(tag + " : "+message);
		}
	}

	public static BufferedWriter out;
	@SuppressWarnings("deprecation")
	private static void createFileOnDevice(Boolean append) throws IOException {
		if(!isSaveToFileEnabled) return;
		File root = Environment.getExternalStorageDirectory();
		if(root.canWrite() && isExternalStorageWritable()){
			File logDir = new File(root + "/com.philips.purair/logs");
			if(!logDir.exists()) {
				logDir.mkdirs();
			}
			File  logFile = new File(logDir.getPath(), "Log.txt");
			FileWriter logWriter = new FileWriter(logFile, append);
			out = new BufferedWriter(logWriter);
			Date date = new Date();
			out.write("Logged at" + String.valueOf(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\n"));
		}
	}

	private static boolean isExternalStorageWritable() {
		if(!isSaveToFileEnabled) return false;
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	private static void writeToFile(String message){
		if(!isSaveToFileEnabled) return;
		File root = Environment.getExternalStorageDirectory();
		if(!root.canWrite() || !isExternalStorageWritable()) return;
		try {
			out.write(message+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void finishLoggingToFile() {
		if(!isSaveToFileEnabled) return;
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

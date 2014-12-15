package com.philips.cl.di.dev.pa.digitalcare.util;

import android.util.Log;

/**
 * Custom log class: - Defines all log tags - Intercepts logs so more processing
 * is possible
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2014
 */
public class ALog {

	public static final String ERROR = "Error"; // Use to log errors
	public static final String APPLICATION = "DigitalCareApp";
	public static final String ACTIVITY = "ActivityLifecycle";
	public static final String FRAGMENT = "FragmentLifecycle";
	public static final String DIGICAREACTIVITY = "DigitalCareActivity";

	private static boolean isLoggingEnabled = true;

//	private static boolean isSaveToFileEnabled = false;

/*	public static void initLoggingToFile() {
		if (!isSaveToFileEnabled)
			return;
		try {
			createFileOnDevice(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

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
		//	writeToFile(tag + " : " + message);
		}
	}

	public static void e(String tag, String message) {
		if (isLoggingEnabled) {
			Log.e(tag, message);
			//writeToFile(tag + " : " + message);
		}
	}

	public static void i(String tag, String message) {
		if (isLoggingEnabled) {
			Log.i(tag, message);
			//writeToFile(tag + " : " + message);
		}
	}

	public static void v(String tag, String message) {
		if (isLoggingEnabled) {
			Log.v(tag, message);
		//	writeToFile(tag + " : " + message);
		}
	}

	public static void w(String tag, String message) {
		if (isLoggingEnabled) {
			Log.w(tag, message);
			//writeToFile(tag + " : " + message);
		}
	}

/*	public static BufferedWriter out;

	private static void createFileOnDevice(Boolean append) throws IOException {
		if (!isSaveToFileEnabled)
			return;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite() && isExternalStorageWritable()) {
			File logDir = new File(root + "/com.philips.purair/logs");
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			File logFile = new File(logDir.getPath(), "Log.txt");
			FileWriter logWriter = new FileWriter(logFile, append);
			out = new BufferedWriter(logWriter);
			Date date = new Date();
			out.write("Logged at"
					+ String.valueOf(date.getHours() + ":" + date.getMinutes()
							+ ":" + date.getSeconds() + "\n"));
		}
	}*/
/*
	private static boolean isExternalStorageWritable() {
		if (!isSaveToFileEnabled)
			return false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}*/
/*
	private static void writeToFile(String message) {
		if (!isSaveToFileEnabled)
			return;
		File root = Environment.getExternalStorageDirectory();
		if (!root.canWrite() || !isExternalStorageWritable())
			return;
		try {
			out.write(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void finishLoggingToFile() {
		if (!isSaveToFileEnabled)
			return;
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}

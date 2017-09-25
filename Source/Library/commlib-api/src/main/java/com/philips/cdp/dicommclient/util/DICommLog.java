/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Custom log class:
 * - Defines all log tags
 * - Intercepts logs so more processing is possible
 *
 * @author jmols
 */
public class DICommLog {

    public enum Verbosity {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    public static final String WIFI = "WifiNetworks ";
    public static final String ICPCLIENT = "IcpClient";
    public static final String SECURITY = "DISecurity";
    public static final String SSDP = "SSDP";
    public static final String KPS = "KPS";
    public static final String DATABASE = "DatabaseAir";
    public static final String CPPCONTROLLER = "CppController";
    public static final String INDOOR_RDCP = "IndoorRdcp";
    public static final String PAIRING = "Pairing";
    public static final String SUBSCRIPTION = "Subscription";
    public static final String PARSER = "DataParser";
    public static final String DISCOVERY = "LanDiscovery";
    public static final String NETWORKMONITOR = "NetworkMonitor";
    public static final String UDP = "UDPSocket";
    public static final String SSDPHELPER = "SsdpHelper";
    public static final String CPPDISCHELPER = "CppDiscoveryHelper";
    public static final String APP_START_UP = "AppStartUp";
    public static final String APPLIANCE = "Appliance";
    public static final String DEVICEPORT = "DevicePort";
    public static final String WIFIPORT = "WifiPort";
    public static final String WIFIUIPORT = "WifiUIPort";
    public static final String PAIRINGPORT = "PairingPort";
    public static final String FIRMWAREPORT = "FirmwarePort";
    public static final String SCHEDULELISTPORT = "ScheduleListPort";
    public static final String LOCALREQUEST = "LanRequest";
    public static final String REMOTEREQUEST = "RemoteRequest";
    public static final String REQUESTQUEUE = "RequestQueue";
    public static final String LOCAL_SUBSCRIPTION = "LocalSubscription";
    public static final String REMOTE_SUBSCRIPTION = "RemoteSubscription";
    public static final String APPLIANCE_MANAGER = "ApplianceManager";
    public static final String UDPRECEIVER = "UdpEventReceiver";

    private static boolean isLoggingEnabled = true;

    private static boolean isSaveToFileEnabled = false;

    public static void initLoggingToFile() {
        if (!isSaveToFileEnabled) return;
        try {
            createFileOnDevice(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(final @NonNull Verbosity verbosity, final @NonNull String logTag, final @NonNull String logMessage) {
        switch (verbosity) {
            case VERBOSE:
                v(logTag, logMessage);
                break;
            case DEBUG:
                d(logTag, logMessage);
                break;
            case INFO:
                i(logTag, logMessage);
                break;
            case WARN:
                w(logTag, logMessage);
                break;
            case ERROR:
                e(logTag, logMessage);
                break;
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
            writeToFile(tag + " : " + message);
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
            writeToFile(tag + " : " + message);
        }
    }

    public static void w(String tag, String message) {
        if (isLoggingEnabled) {
            Log.w(tag, message);
            writeToFile(tag + " : " + message);
        }
    }

    public static BufferedWriter out;

    @SuppressWarnings("deprecation")
    private static void createFileOnDevice(Boolean append) throws IOException {
        if (!isSaveToFileEnabled) return;
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
            out.write("Logged at" + String.valueOf(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\n"));
        }
    }

    private static boolean isExternalStorageWritable() {
        if (!isSaveToFileEnabled) return false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static void writeToFile(String message) {
        if (!isSaveToFileEnabled) return;
        File root = Environment.getExternalStorageDirectory();
        if (!root.canWrite() || !isExternalStorageWritable()) return;
        try {
            out.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finishLoggingToFile() {
        if (!isSaveToFileEnabled) return;
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

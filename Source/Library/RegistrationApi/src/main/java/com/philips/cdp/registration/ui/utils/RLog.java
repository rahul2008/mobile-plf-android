
package com.philips.cdp.registration.ui.utils;

import android.util.Log;

import com.janrain.android.engage.JREngage;
import com.philips.dhpclient.util.HsdpLog;
import com.philips.platform.appinfra.logging.LoggingInterface;

public class RLog {

    public static final String FRAGMENT_LIFECYCLE = "FragmentLifecycle";

    public static final String ACTIVITY_LIFECYCLE = "ActivityLifecycle";

    public static final String EVENT_LISTENERS = "EventListeners";

    public static final String APPLICATION = "RegistrationApplication";

    public static final String NETWORK_STATE = "NetworkState";

    public static final String JANRAIN_INITIALIZE = "JanrainInitialize";

    public static final String VERSION = "Version";

    public static final String EXCEPTION = "Exception";

    public static final String ONCLICK = "onClick";

    public static final String CALLBACK = "CallBack";

    public static final String ANALYTICS = "Analytics";

    public static final String HSDP = "Hsdp";

    private static boolean isLoggingEnabled;

    private static LoggingInterface mLoggingInterface;

 	public static final String SERVICE_DISCOVERY = "ServiceDiscovery";

    public static final String AB_TESTING= "AB Testing";
    public static void init() {
        mLoggingInterface  = URInterface.getComponent().getLoggingInterface();
    }

    public static void enableLogging() {
        HsdpLog.enableLogging();
        isLoggingEnabled = true;
        JREngage.isLoggingEnabled = Boolean.TRUE;
    }

    public static void disableLogging() {
        HsdpLog.disableLogging();
        isLoggingEnabled = false;
        JREngage.isLoggingEnabled = Boolean.FALSE;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    private static void validateLoggerInitialization() {
        if (mLoggingInterface == null) {
            throw new RuntimeException("Please initiate AppInfra Logger by calling RLog.init()");
        }
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
            validateLoggerInitialization();
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
            validateLoggerInitialization();
            mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
            validateLoggerInitialization();
            mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
            validateLoggerInitialization();
            mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
        }
    }

}

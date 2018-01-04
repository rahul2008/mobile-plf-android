
package com.philips.cdp.registration.ui.utils;

import android.support.annotation.*;
import android.util.Log;

import com.janrain.android.engage.JREngage;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.dhpclient.util.HsdpLog;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

/**
 * Registration loggger used for logginan system .
 * Can enable or desable logs and can be level of i,v,w etc.
 */
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

    public static final String AB_TESTING = "AB Testing";

    private static AppTaggingInterface mAppTaggingInterface;

    /**
     * Initialize the logger with AppInfra logger Taken care by USR coponent no need to call explicitly
     */
    public static void init() {
        mLoggingInterface = RegistrationConfiguration.getInstance().getComponent().getLoggingInterface();
        mLoggingInterface = mLoggingInterface.createInstanceForComponent("usr", RegistrationHelper.getRegistrationApiVersion());
        mAppTaggingInterface = RegistrationConfiguration.getInstance().getComponent().getAppTaggingInterface();
    }

    /**
     * Enable logging
     */
    public static void enableLogging() {
        HsdpLog.enableLogging();
        isLoggingEnabled = true;
        JREngage.isLoggingEnabled = Boolean.TRUE;
    }

    /**
     * Disable logging
     */
    public static void disableLogging() {
        HsdpLog.disableLogging();
        isLoggingEnabled = false;
        JREngage.isLoggingEnabled = Boolean.FALSE;
    }

    /**
     * Status of logs enabled or dissabled
     *
     * @return true if enabled else false.
     */
    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    private static void validateLoggerInitialization() {
        if (mLoggingInterface == null) {
            throw new RuntimeException("Please initiate AppInfra Logger by calling RLog.init()");
        }
    }

    /**
     * Logs at debug level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
    }
    /**
     * Logs at error level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
    }

    /**
     * Logs at info level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
    }
    /**
     * Logs at verbose level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
    }

    @VisibleForTesting
    public static void setMockLogger(LoggingInterface mockLoggingInterface) {
        mLoggingInterface = mockLoggingInterface;
    }
}

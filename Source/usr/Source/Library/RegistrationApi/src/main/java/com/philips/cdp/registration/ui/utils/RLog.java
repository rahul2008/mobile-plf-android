
package com.philips.cdp.registration.ui.utils;

import androidx.annotation.VisibleForTesting;

import com.janrain.android.engage.JREngage;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * Registration loggger used for logginan system .
 * Can enable or desable logs and can be level of i,v,w etc.
 */
public class RLog {

    private static LoggingInterface mLoggingInterface;

    /**
     * Initialize the logger with AppInfra logger Taken care by USR coponent no need to call explicitly
     */
    public static void init() {
        mLoggingInterface = RegistrationConfiguration.getInstance().getComponent().getLoggingInterface();
        mLoggingInterface = mLoggingInterface.createInstanceForComponent(RegConstants.COMPONENT_TAGS_ID, RegistrationHelper.getRegistrationApiVersion());
    }

    /**
     * Enable logging
     */
    public static void enableLogging() {
        JREngage.isLoggingEnabled = Boolean.TRUE;
    }

    /**
     * Disable logging
     */
    public static void disableLogging() {
        JREngage.isLoggingEnabled = Boolean.FALSE;
    }


    /**
     * Logs at debug level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void d(String tag, String message) {
        if (mLoggingInterface == null) return;
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
    }

    /**
     * Logs at error level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void e(String tag, String message) {
        if (mLoggingInterface == null) return;
        mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
    }

    /**
     * Logs at info level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void i(String tag, String message) {
        if (mLoggingInterface == null) return;
        mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
    }

    /**
     * Logs at verbose level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void v(String tag, String message) {
        if (mLoggingInterface == null) return;
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
    }

    @VisibleForTesting
    public static void setMockLogger(LoggingInterface mockLoggingInterface) {
        mLoggingInterface = mockLoggingInterface;
    }
}

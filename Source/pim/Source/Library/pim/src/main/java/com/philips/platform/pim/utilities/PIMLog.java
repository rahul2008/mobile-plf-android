package com.philips.platform.pim.utilities;

import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.BuildConfig;
import com.philips.platform.pim.configration.PIMConfiguration;

public class PIMLog {
    private static LoggingInterface mLoggingInterface;

    /**
     * Initialize the logger with AppInfra logger Taken care by USR coponent no need to call explicitly
     */
    public static void init() {
        mLoggingInterface = PIMConfiguration.getInstance().getComponent().getLoggingInterface();
        mLoggingInterface = mLoggingInterface.createInstanceForComponent(PIMConstants.COMPONENT_TAGS_ID, BuildConfig.VERSION_NAME);
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

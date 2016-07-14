/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : SaecoAvanti
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.utils;

import com.philips.platform.appinfra.logging.LoggingInterface;

public class IAPLog {
    public final static String LOG = "InAppPurchase";
    public static final String FRAGMENT_LIFECYCLE = "FragmentLifecycle";
    public static final String BASE_FRAGMENT_ACTIVITY = "IAPActivity";
    public static final String DEMOAPPACTIVITY = "DemoAppActivity";

    public static boolean isLoggingEnabled = false;

    private static LoggingInterface mIAPLoggingInterface = AppInfraHelper.getInstance().getIAPLoggingInterfaceInterface();

    public static void enableLogging(boolean enableLog) {
        mIAPLoggingInterface.enableConsoleLog(enableLog);
        mIAPLoggingInterface.enableFileLog(enableLog);
        isLoggingEnabled = enableLog;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            mIAPLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,tag,message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            mIAPLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            mIAPLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            mIAPLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
        }
    }
}


/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.util.Log;

import com.philips.cdp.registration.settings.RegistrationHelper;
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

    private static boolean isLoggingEnabled = true;

    private static LoggingInterface mLoggingInterface;
    private static Context mContext;

    public static void enableLogging() {
        isLoggingEnabled = true;
    }

    public static void init(final Context context){
        mContext = context;

        mLoggingInterface =  RegistrationHelper.getInstance().getAppInfraInstance().getLogging().createInstanceForComponent("Registration","Registration");
        mLoggingInterface.enableConsoleLog(false);
        mLoggingInterface.enableFileLog(true);

    }

    public static void initForTesting(final Context context){
        mContext = context;

        mLoggingInterface =  RegistrationHelper.getInstance().getAppInfraInstance().getLogging().createInstanceForComponent("Registration","Registration");
        mLoggingInterface.enableConsoleLog(false);
        mLoggingInterface.enableFileLog(false);
    }

    public static void disableLogging() {
        isLoggingEnabled = false;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    private static void validateLoggerInitialization(){
        if(mLoggingInterface == null){
            throw new RuntimeException("Please initiate AppInfra Logger by calling RLog.init()");
        }
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
            validateLoggerInitialization();
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,tag,message);
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

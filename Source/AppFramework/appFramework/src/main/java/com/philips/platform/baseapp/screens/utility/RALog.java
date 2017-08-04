package com.philips.platform.baseapp.screens.utility;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * Created by philips on 19/04/17.
 */

public class RALog {
    private static LoggingInterface mLoggingInterface;

    public static void init(AppInfraInterface appInfraInterface) {
        mLoggingInterface = appInfraInterface.getLogging();
    }


    public static void d(String tag, String message) {
        if(mLoggingInterface!=null) {
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(mLoggingInterface!=null) {
            mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
        }
    }

    public static void i(String tag, String message) {
        if(mLoggingInterface!=null)
        {
            mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);}
    }

    public static void v(String tag, String message) {
        if(mLoggingInterface!=null) {
            mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
        }
    }
}

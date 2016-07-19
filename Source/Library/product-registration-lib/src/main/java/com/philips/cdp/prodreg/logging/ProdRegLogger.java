package com.philips.cdp.prodreg.logging;

import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegLogger {

    private final static LoggingInterface mAppInfraLogger = AppInfraSingleton.getInstance() != null ? AppInfraSingleton.getInstance().getLogging() : null;

    private ProdRegLogger() {
    }

    public static void v(String tag, String msg) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.VERBOSE, tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static void d(String tag, String msg) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.DEBUG, tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.DEBUG, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static void i(String tag, String msg) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.INFO, tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.INFO, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static void w(String tag, String msg) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.WARNING, tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.WARNING, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static void e(String tag, String msg) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.ERROR, tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (mAppInfraLogger != null) {
            mAppInfraLogger.log(LoggingInterface.LogLevel.ERROR, tag, msg + '\n' + getStackTraceString(tr));
        }
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}

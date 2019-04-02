/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.cdp.prodreg.logging;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

@SuppressWarnings("deprecation")
public class ProdRegLogger {
    private static LoggingInterface mLoggingInterface;

    public static void init(LoggingInterface pLoggingInterface) {
        mLoggingInterface = pLoggingInterface.createInstanceForComponent("prg", BuildConfig.VERSION_NAME);
    }

    public static void v(String tag, String msg) {
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void d(String tag, String msg) {

        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void i(String tag, String msg) {
        mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void w(String tag, String msg) {
        mLoggingInterface.log(LoggingInterface.LogLevel.WARNING, tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        mLoggingInterface.log(LoggingInterface.LogLevel.WARNING, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static void e(String tag, String msg) {
        mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, msg + '\n' + getStackTraceString(tr));
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

    /*public static void init(Context context) {
        if (AppInfraSingleton.getInstance() != null && AppInfraSingleton.getInstance().getLogging() != null) {
            mAppInfraLogger = AppInfraSingleton.getInstance().getLogging().createInstanceForComponent("Product Registration", BuildConfig.VERSION_NAME);
            if (mAppInfraLogger != null) {
                boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
                if (isDebuggable) {
                    mAppInfraLogger.enableConsoleLog(true);
                    mAppInfraLogger.enableFileLog(true);
                } else {
                    mAppInfraLogger.enableConsoleLog(false);
                    mAppInfraLogger.enableFileLog(false);
                }
            }
        }
    }*/
}

package com.philips.platform.appinfra.logging;

import android.text.TextUtils;

import java.util.logging.Level;

/**
 * Created by abhishek on 4/24/18.
 */

public class LoggingUtils {
   public static  Level getJavaLoggerLogLevel(String level) {
        final Level javaLevel;
        if (!TextUtils.isEmpty(level)) {
            switch (level) {
                case "ERROR":
                case "Error":
                case "error":
                    javaLevel = Level.SEVERE;
                    break;
                case "WARN":
                case "Warn":
                case "warn":
                    javaLevel = Level.WARNING;
                    break;
                case "INFO":
                case "Info":
                case "info":
                    javaLevel = Level.INFO;
                    break;
                case "DEBUG":
                case "Debug":
                case "debug":
                    javaLevel = Level.CONFIG;
                    break;
                case "VERBOSE":
                case "Verbose":
                case "verbose":
                    javaLevel = Level.FINE;
                    break;
                case "ALL":
                case "All":
                case "all":
                    javaLevel = Level.FINE;
                    break;
                case "OFF":
                case "Off":
                case "off":
                    javaLevel = Level.OFF;
                    break;
                default:
                    javaLevel = Level.FINE;
            }

            return javaLevel;
        }
        return Level.FINE;
    }
}

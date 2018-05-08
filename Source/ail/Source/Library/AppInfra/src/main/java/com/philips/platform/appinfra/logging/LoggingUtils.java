package com.philips.platform.appinfra.logging;

import android.os.Build;
import android.text.TextUtils;

import java.util.UUID;
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

    public static String getUUID(){
       return UUID.randomUUID().toString();
    }

    public static  String getAILLogLevel(String level) {
        if (!TextUtils.isEmpty(level)) {
            switch (level) {
                case "SEVERE":
                    return "ERROR";
                case "WARNING":
                    return "WARNING";
                case "INFO":
                    return "INFO";
                case "CONFIG":
                    return "DEBUG";
                case "FINE":
                    return "VERBOSE";
                case "OFF":
                    return "OFF";
                default:
                    return "VERBOSE";
            }
        }else {
            return "VERBOSE";
        }
    }

    public static String getOSVersion(){
        return Build.VERSION.RELEASE;
    }

    public static int getStringLengthInBytes(String string){
        if(TextUtils.isEmpty(string)){
            return 0;
        }
        return string.toCharArray().length * 2;
    }

}

/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A Formatter provides support for formatting LogRecords.
 * Typically each logging Handler will have a Formatter associated
 * with it.  The Formatter takes a LogRecord and converts it to
 * a string.
 */
public class LogFormatter extends Formatter {
    // Create a DateFormat to format the logger ;.
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.ENGLISH);
    private final String componentNameAndVersion;
    String mComponentName = "NA";
    String mComponentVersion = "NA";
    AppInfra mappInfra;

    public LogFormatter(String ComponentName, String componentVersion, AppInfra mAppinfra) {
        mappInfra = mAppinfra;
        try {
            if (null != ComponentName) {
                mComponentName = ComponentName;
            } else {
                if (mAppinfra.getAppIdentity() != null)
                    mComponentName = mAppinfra.getAppIdentity().getAppName();
            }
            if (null != componentVersion) {
                mComponentVersion = componentVersion;
            } else {
                if (mAppinfra.getAppIdentity() != null)
                    mComponentVersion = mAppinfra.getAppIdentity().getAppVersion();
            }
        } catch (IllegalArgumentException e) {
            mAppinfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LOGGING,"Logging"+e.getMessage());
        }

        componentNameAndVersion = mComponentName + " " + mComponentVersion;
    }

    public String format(LogRecord record) {
        Map<?,?> dictionary = null;
        final StringBuilder builder = new StringBuilder(1000);
        builder.append("[");
        if (mappInfra != null && mappInfra.getTime() != null) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
            builder.append(dateFormat.format(mappInfra.getTime().getUTCTime())).append("]");
        }


        final Level logLevel = record.getLevel();
        String logLevelPrettyName = logLevel.toString();
        if (logLevel == Level.SEVERE) {
            logLevelPrettyName = "ERROR";
        } else if (logLevel == Level.CONFIG) {
            logLevelPrettyName = "DEBUG";
        } else if (logLevel == Level.FINE) {
            logLevelPrettyName = "VERBOSE";
        }
        builder.append("[").append(logLevelPrettyName).append("]");
        builder.append("[").append(componentNameAndVersion).append("]");
        builder.append("[").append(formatMessage(record)).append("]"); // this we assume as event
        //builder.append("[").append(record.getSourceClassName()).append("] ");
        //builder.append("[").append(record.getSourceMethodName()).append("] ");
        final Object[] params = record.getParameters(); // this we assume as message
        String eventName = "NA";// Default event name
        if (null != params && params.length > 0) {
            eventName = (String) params[0];  // params[0] is message
            if (params.length == 2) {
                try {
                    if (params[1] instanceof Map)
                        dictionary = (Map) params[1];
                } catch (Exception e) {
                    mappInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, AppInfraLogEventID.AI_LOGGING, "Not a valid Map(Dictionary)");
                }
            }
        }
        builder.append("[").append(eventName).append("]");
        if (null != dictionary) {
            builder.append("[").append(dictionary).append("]"); // optional Map/dictionary
        }
        builder.append("\n");
        return builder.toString();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}

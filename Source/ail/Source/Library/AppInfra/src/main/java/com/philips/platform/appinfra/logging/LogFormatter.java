/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.timesync.TimeInterface;

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
    AppInfraInterface mappInfra;

    public LogFormatter(AppInfraInterface mAppinfra) {
        mappInfra = mAppinfra;
    }

    public String format(LogRecord record) {
        Map<?,?> dictionary = null;
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (mappInfra != null && mappInfra.getTime() != null) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
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
        final Object[] params = record.getParameters(); // this we assume as message
        String eventName = "NA";// Default event name
        String componentID="";
        String componentVersion="";
        if (null != params && params.length > 0) {
            //Getting log message from index 0.
            eventName = (String) params[AppInfraLogging.LOG_MESSAGE_INDEX];  // params[0] is message
            //Getting component Id and Component version from 1 and 2 index respectively.
            componentID=(String)params[AppInfraLogging.COMPONENT_ID_INDEX];
            componentVersion=(String)params[AppInfraLogging.COMPONENT_VERSION_INDEX];

            if (params.length == AppInfraLogging.PARAM_SIZE_WITH_METADATA) {
                try {
                    if (params[AppInfraLogging.LOG_METADATA_INDEX] instanceof Map)
                        dictionary = (Map) params[AppInfraLogging.LOG_METADATA_INDEX];
                } catch (Exception e) {
                    ((AppInfra)mappInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, AppInfraLogEventID.AI_LOGGING, "Not a valid Map(Dictionary)");
                }
            }
        }
        builder.append("[").append(logLevelPrettyName).append("]");
        builder.append("[").append(componentID + " " + componentVersion).append("]");
        builder.append("[").append(formatMessage(record)).append("]"); // this we assume as event

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

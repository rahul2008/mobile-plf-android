/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by 310238114 on 4/26/2016.
 */
public class LogFormatter extends Formatter {
private final String componentNameAndVersion;
    public LogFormatter(String ComponentName, String componentVersion){
        componentNameAndVersion=ComponentName+" "+componentVersion;
    }

    // Create a DateFormat to format the logger ;.
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.ENGLISH);
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append("[");
        Date aiCurrentDate= new Date(record.getMillis());
        builder.append(DATE_FORMAT.format(aiCurrentDate)).append("]");
        String componentName = "NA"; // Default component name
        /*if(null!=record.getLoggerName()){
            componentName=record.getLoggerName();
        }*/
        builder.append("[").append(componentNameAndVersion).append("]");
        Level logLevel = record.getLevel();
        String logLevelPrettyName = logLevel.toString();
        if(logLevel == Level.SEVERE){
            logLevelPrettyName="ERROR";
        }else if(logLevel == Level.CONFIG){
            logLevelPrettyName="DEBUG";
        }else if(logLevel == Level.FINE){
            logLevelPrettyName="VERBOSE";
        }
        builder.append("[").append(logLevelPrettyName).append("]");
        //builder.append("[").append(record.getSourceClassName()).append("] ");
        //builder.append("[").append(record.getSourceMethodName()).append("] ");
        Object[] eventNameList= record.getParameters();
        String eventName = "NA";// Default event name
        if(null!=eventNameList && eventNameList.length>0){
            eventName=(String)eventNameList[0];
        }
        builder.append("[").append(eventName).append("]");
        builder.append("[").append(formatMessage(record)).append("]");;
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

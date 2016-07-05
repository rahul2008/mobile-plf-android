/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    String mComponentName="NA";
    String mComponentVersion="NA";
    AppInfra mappInfra;
    public LogFormatter(String ComponentName, String componentVersion, AppInfra mAppinfra){
        mappInfra= mAppinfra;
        if(null!=ComponentName){
            mComponentName=ComponentName;
        }
        if(null!=componentVersion){
            mComponentVersion=componentVersion;
        }
        componentNameAndVersion=mComponentName+" "+mComponentVersion;
    }

    // Create a DateFormat to format the logger ;.
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.ENGLISH);
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append("[");
        if(mappInfra != null && mappInfra.getTimeSync()!=null){
            builder.append(mappInfra.getTimeSync().getUTCTime()).append("]");
        }

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
        builder.append("[").append(formatMessage(record)).append("]"); // this we assume as event
        //builder.append("[").append(record.getSourceClassName()).append("] ");
        //builder.append("[").append(record.getSourceMethodName()).append("] ");
        Object[] eventNameList= record.getParameters(); // this we assume as message
        String eventName = "NA";// Default event name
        if(null!=eventNameList && eventNameList.length>0){
            eventName=(String)eventNameList[0];
        }
        builder.append("[").append(eventName).append("]");

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

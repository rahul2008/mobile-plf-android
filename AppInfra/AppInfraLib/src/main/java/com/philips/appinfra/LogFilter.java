/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.appinfra;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created by 310238114 on 5/11/2016.
 */
public class LogFilter implements Filter {
    String mComponentName;
    String mEventName;
public LogFilter(String componentName, String eventName){
    mComponentName = componentName;
    mEventName = eventName;
}
    @Override
    public boolean isLoggable(LogRecord record) {
        boolean result = false;
        Object[] logObjects = record.getParameters();
        if(null!=mComponentName){
            if (mComponentName.equalsIgnoreCase(record.getLoggerName())){
                result=true;
            }
        }

        if(null!=mEventName){
                Object[] eventNameList= record.getParameters();
                String eventName = "";
                if(null!=eventNameList && eventNameList.length>0){
                    eventName=(String)eventNameList[0];
                }
            if(eventName.contains(mEventName)){
                result=true;
            }
        }



        return result;
    }
}

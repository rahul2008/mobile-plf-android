/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.appinfra;


/**
 * Created by 310238114 on 5/5/2016.
 */
public class AILogging implements  LoggingInterface {
      AppInfra mAppInfra;

    /*public AILogging() {
    // default constructor
    }*/

    public AILogging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

    @Override
    public LoggingInterface createInstanceForComponent(String componentId, String componentVersion) {
       return new LoggingWrapper(mAppInfra, componentId, componentVersion);
        //return null;
    }

    @Override
    public void log(LogLevel level, String eventId, String message) {
        log(level, eventId, null, null, message);
    }

    @Override
    public void enableFileLog(boolean pFileLogEnabled) {

    }

    @Override
    public void enableConsoleLog(boolean isEnabled) {

    }

    protected void log(LogLevel level, String componentId, String componentVersion, String eventId, String message) {
        // TODO: do something smart
    }
}

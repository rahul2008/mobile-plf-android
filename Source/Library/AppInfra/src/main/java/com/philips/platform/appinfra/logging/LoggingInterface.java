/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.logging;


public interface LoggingInterface {
    /**
     * The enum Log level.
     */
    public  enum  LogLevel {VERBOSE, DEBUG, INFO, WARNING, ERROR};

    /**
     * Create instance for component logging interface.
     * This method to be used by all component to get their respective logging
     * @param componentId      the base package name of component
     * @param componentVersion the component version
     * @return the logging interface
     */
    public LoggingInterface createInstanceForComponent(String componentId, String componentVersion);

    /**
     * Logs message on console and file .
     * @param level   the level {VERBOSE, DEBUG, INFO, WARNING, ERROR}
     * @param eventId the Event name or Tag
     * @param message the message
     */
    public void log(LogLevel level, String eventId, String message);

    /**
     * Enable file log of application.
     * @param enable File
     */
    @Deprecated
    public void enableFileLog(boolean enable);

    /**
     * Enable console log of application.
     * @param enable Console
     */
    @Deprecated
    public void enableConsoleLog(boolean enable );

}

package com.philips.platform.dscdemo.database;

import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Map;

class LoggingInterfaceStub implements LoggingInterface {

    private LoggingInterfaceStub loggingInterfaceStub;

    @Override
    public LoggingInterface createInstanceForComponent(final String componentId, final String componentVersion) {
        loggingInterfaceStub = new LoggingInterfaceStub();
        return loggingInterfaceStub;
    }

    @Override
    public void log(final LogLevel level, final String eventId, final String message) {

    }

    @Override
    public void log(final LogLevel level, final String eventId, final String message, final Map<String, ?> map) {

    }
}

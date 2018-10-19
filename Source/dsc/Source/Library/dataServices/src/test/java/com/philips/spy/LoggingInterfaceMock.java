package com.philips.spy;

import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Map;

public class LoggingInterfaceMock implements LoggingInterface {
    @Override
    public LoggingInterface createInstanceForComponent(final String componentId, final String componentVersion) {
        return null;
    }

    @Override
    public void log(final LogLevel level, final String eventId, final String message) {

    }

    @Override
    public void log(final LogLevel level, final String eventId, final String message, final Map<String, ?> map) {

    }

    @Override
    public void setHSDPUserUUID(final String userUUID) {

    }

    @Override
    public String getCloudLoggingConsentIdentifier() {
        return null;
    }
}

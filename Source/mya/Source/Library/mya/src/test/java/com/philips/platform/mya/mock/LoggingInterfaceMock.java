/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Map;

public class LoggingInterfaceMock implements LoggingInterface {
    @Override
    public LoggingInterface createInstanceForComponent(String s, String s1) {
        return this;
    }

    @Override
    public void log(LogLevel logLevel, String s, String s1) {

    }

    @Override
    public void log(LogLevel logLevel, String s, String s1, Map<String, ?> map) {

    }
}

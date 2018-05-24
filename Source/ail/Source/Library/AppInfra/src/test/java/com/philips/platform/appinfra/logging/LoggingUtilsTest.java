package com.philips.platform.appinfra.logging;

import junit.framework.TestCase;

import java.util.logging.Level;

/**
 * Created by philips on 5/2/18.
 */
public class LoggingUtilsTest extends TestCase {


    public void testLogLevel() {
        assertEquals(Level.SEVERE, LoggingUtils.getJavaLoggerLogLevel("error"));
        assertEquals(Level.WARNING, LoggingUtils.getJavaLoggerLogLevel("Warning"));
        assertEquals(Level.INFO, LoggingUtils.getJavaLoggerLogLevel("info"));
        assertEquals(Level.CONFIG, LoggingUtils.getJavaLoggerLogLevel("debug"));
        assertEquals(Level.FINE, LoggingUtils.getJavaLoggerLogLevel("verbose"));
        assertEquals(Level.FINE, LoggingUtils.getJavaLoggerLogLevel("all"));
        assertEquals(Level.FINE, LoggingUtils.getJavaLoggerLogLevel("fine"));
        assertEquals(Level.OFF, LoggingUtils.getJavaLoggerLogLevel("off"));
    }

    public void testAILogLevel() {
        assertEquals(LoggingUtils.getAILLogLevel("SEVERE"), "ERROR");
        assertEquals(LoggingUtils.getAILLogLevel("WARNING"), "WARNING");
        assertEquals(LoggingUtils.getAILLogLevel("INFO"), "INFO");
        assertEquals(LoggingUtils.getAILLogLevel("CONFIG"), "DEBUG");
        assertEquals(LoggingUtils.getAILLogLevel("FINE"), "VERBOSE");
        assertEquals(LoggingUtils.getAILLogLevel("OFF"), "OFF");
        assertEquals(LoggingUtils.getAILLogLevel(""), "VERBOSE");
    }


    public void testUUID() {
        assertNotNull(LoggingUtils.getUUID());
    }

    public void testGetStringLengthInBytes() {
        assertEquals(LoggingUtils.getStringLengthInBytes(""), 0);
        assertEquals(LoggingUtils.getStringLengthInBytes("hi"), 4);
    }

    public void testConvertObjectToJsonString() {
        assertNotNull(LoggingUtils.convertObjectToJsonString("data"));
        assertNull(LoggingUtils.convertObjectToJsonString(null));
    }

    public void testGetCurrentDateAndTime() {
        assertNotNull(LoggingUtils.getCurrentDateAndTime("yyyy:dd:mm"));
    }

    public void testgetFormattedDateAndTime() {
        assertNotNull(LoggingUtils.getFormattedDateAndTime(123456789,"yyyy:dd:mm"));
    }
}
//package com.philips.dhpclient.util;
//
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
//
//public final class DateTimeUtils {
//
//    private DateTimeUtils() {
//    }
//
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC();
//    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZoneUTC();
//
//    public static DateTime toDateTime(String value) {
//        try {
//            return DATE_TIME_FORMATTER.parseDateTime(value);
//        } catch (IllegalArgumentException ex) {
//            return DATE_FORMATTER.parseDateTime(value);
//        }
//    }
//}
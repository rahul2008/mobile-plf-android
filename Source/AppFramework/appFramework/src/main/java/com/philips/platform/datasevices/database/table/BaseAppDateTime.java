/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasevices.database.table;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppDateTime {
    @Inject
    public BaseAppDateTime() {

    }

    public final static String DEFAULT_DATE_FORMATTER = "yyyy-MM-dd";
    public final String DATE_FORMATTER = "dd MMMM yyyy";

    public DateTime newDateTime(String dateString, final String format) {
        DateTime dateTime = DateTime.now();
        final DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date date = dateFormat.parse(dateString);
            LocalDateTime localDateTime = LocalDateTime.fromDateFields(date);
            dateTime = localDateTime.toDateTime(dateTimeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public Date getMaximumDate(final Date localDate) {
        Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();
        DateTime plusDateTime = new DateTime(localDate).plusDays(1);
        final DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        try {
            LocalDateTime localDateTime = newDateTime(plusDateTime.toString(), DEFAULT_DATE_FORMATTER).plusMinutes(30).toLocalDateTime();
            return localDateTime.toDateTime(dateTimeZone).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public DateTime getNextDayDateTime(final Date localDate) {
        DateTime dateTime = new DateTime(localDate);
        DateTime plusDateTime = new DateTime(localDate).plusDays(1);
        final DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        try {
            LocalDateTime localDateTime = newDateTime(plusDateTime.toString(), DEFAULT_DATE_FORMATTER).toLocalDateTime();
            return localDateTime.toDateTime(dateTimeZone).toDateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}

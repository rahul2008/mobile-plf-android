/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database.table;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppDateTime {
    @Inject
    public BaseAppDateTime() {

    }

  //  public final static String DEFAULT_DATE_FORMATTER = "yyyy-MM-dd";

    public DateTime newDateTime(String dateString) {
        DateTime dateTime = DateTime.now();
        try {
            final LocalDateTime localDateTime = LocalDateTime.
                    fromDateFields(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).
                            parse(dateString));
            dateTime = localDateTime.toDateTime(DateTimeZone.getDefault());
        } catch (ParseException e) {
            if (e.getMessage() != null)
                Log.i("***SPO***", "exception = " + e.getMessage());
        }
        return dateTime;
    }

/*    public Date getMaximumDate(final Date localDate) {
        Date date = Calendar.getInstance().getTime();
        DateTime plusDateTime = new DateTime(localDate).plusDays(1);
        try {
            LocalDateTime localDateTime = newDateTime(plusDateTime.toString()).plusMinutes(30).toLocalDateTime();
            return localDateTime.toDateTime(DateTimeZone.getDefault()).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }*/

   /* public DateTime getNextDayDateTime(final Date localDate) {
        DateTime dateTime = new DateTime(localDate);
        DateTime plusDateTime = new DateTime(localDate).plusDays(1);
        try {
            LocalDateTime localDateTime = newDateTime(plusDateTime.toString()).toLocalDateTime();
            return localDateTime.toDateTime(DateTimeZone.getDefault()).toDateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }*/
}

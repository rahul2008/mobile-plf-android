/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp2.ews.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract class DateUtil {

    public static final String BE_SLEEP_SESSION_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DEVICE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String HOURS_AND_MINUTES_ONLY = "h:mm";
    public static final String AM_PM_ONLY = "a";

    private static final String FULL_MONTH_YEAR_FORMAT = "MMMM yyyy";
    private static final String INSIGHT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";  // TODO: check this time format
    private static final String BRIGHT_TIME_FORMAT_AM_PM = "h:mm a";
    public static final String CONSENT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String FALLINGASLEEP_COUNTDOWN_FORMAT = "mm:ss";

    public static String convertDateToISO8601Format(Date date, final String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, getSupportedLocale());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String convertDateToISO8601FormatWithLocalTimeZone(Date date, final String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, getSupportedLocale());
        return dateFormat.format(date);
    }

    @Nullable
    public static Date convertStringToDateISO8601Format(String string) {
        DateFormat dateFormat = new SimpleDateFormat(INSIGHT_DATE_FORMAT, getSupportedLocale());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return dateFormat.parse(string);
        } catch (ParseException ignored) {
        }

        return null;
    }

    /**
     * <p>
     *     <strike>I know that we had issues when using Locale.getDefault()</strike><br />
     *     Now we have specified the locales in gradle, so it will either be "EN" or "DE"
     * </p><p>
     * <code>
     *     defaultConfig {
     *         ...
     *         resConfigs "en", "de"
     *     }
     * </code>
     *  </p>
     * @return {@link Locale#getDefault()}
     */
    public static Locale getSupportedLocale() {
        return Locale.getDefault();
    }

    /**
     * format local for tips
     * @return
     */
    public static Locale getFormatLocale() {
        return Locale.ENGLISH;
    }

    public static String getFormattedFullMonthYear(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(FULL_MONTH_YEAR_FORMAT, getSupportedLocale());
        return formatter.format(date);
    }

    public static String getFormattedFullMonthYear(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Making sure day of month exists in whatever month calendar is created in
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return getFormattedFullMonthYear(calendar.getTime());
    }

    public static Calendar getMonthForTimeStamp(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Month starts at 1
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String getFormattedHoursAndMinutes(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat formatter = new SimpleDateFormat(HOURS_AND_MINUTES_ONLY, getSupportedLocale());
        return formatter.format(calendar.getTime());
    }

    /**
     * Checks the given date is between Today:12pm - Tomorrow:12pm
     */
    static boolean isTodaysSessionDate(final DateTime date) {
        DateTime todayNoon = DateTime.now().withTimeAtStartOfDay().withHourOfDay(12);
        return new Interval(todayNoon, todayNoon.plusDays(1)).contains(date);
    }

    /**
     * NOTE: This will use the Device Locale/Timezone
     * Checks if the sessionMilis belongs to the momentDay
     *
     * @see #sessionBelongsToMoment(DateTime, long, DateTimeZone)
     */
    public static boolean sessionBelongsToMoment(DateTime momentDay, final long sessionMilis) {
        return sessionBelongsToMoment(momentDay, sessionMilis, DateTimeZone.getDefault());
    }

    /**
     * Checks if the sessionMilis belongs to the momentDay
     * <p>
     * You <b>should not</b> have to call this. We always need local time zone,
     * which is the default
     *
     * @see #sessionBelongsToMoment(DateTime, long)
     */
    private static boolean sessionBelongsToMoment(DateTime momentDay, final long sessionMilis, DateTimeZone timeZone) {
        momentDay.withZone(timeZone);
        final DateTime sessionDate = new DateTime(sessionMilis).withZone(timeZone);
        return isDaysSessionDate(momentDay, sessionDate, timeZone);
    }

    private static boolean isDaysSessionDate(final DateTime day, final DateTime dayToCheck, final DateTimeZone timeZone) {
        DateTime daysNoon = getNoonBefore(day, timeZone);
        return new Interval(daysNoon, daysNoon.plusDays(1)).contains(dayToCheck);
    }

    public static DateTime getDayNoon(final DateTime day) {
        return day.withZone(DateTimeZone.getDefault()).withTimeAtStartOfDay().withHourOfDay(12);
    }

    public static DateTime getNoonBefore(final DateTime date) {
        return getNoonBefore(date, DateTimeZone.getDefault());
    }

    /**
     * Returns first noon <b>before</b> this date.
     * You <b>should not</b> have to call this. We always need local time zone,
     * which is the default
     *
     * @see #getNoonBefore(DateTime)
     */
    @VisibleForTesting
    static DateTime getNoonBefore(final DateTime date, final DateTimeZone timeZone) {
        DateTime noon = date.withZone(timeZone).withTimeAtStartOfDay().withHourOfDay(12);

        if (date.withZone(timeZone).getHourOfDay() < 12) {
            noon = noon.minusDays(1);
        }

        return noon;
    }

    public static boolean withinSevenPrecedingDays(@NonNull final DateTime baselineDate, @NonNull final DateTime targetDate) {
        baselineDate.withZone(DateTimeZone.getDefault());
        targetDate.withZone(DateTimeZone.getDefault());
        DateTime earliestIncludedDate = baselineDate.minusDays(7);
        return new Interval(earliestIncludedDate, baselineDate).contains(targetDate);
    }

    public static String getFormattedHoursAndMinutesInAMPM(@NonNull DateTime time) {
        return getDateWithFormat(time,BRIGHT_TIME_FORMAT_AM_PM);
    }

    public static String getFullDateLabel(@NonNull final Date date) {
        return datefromOs(DateFormat.FULL, date);
    }

    public static String getMediumDateLabel(@NonNull final Date date) {
        return datefromOs(DateFormat.MEDIUM, date);
    }

    public static String getMediumDateShortTimeLabel(@NonNull final Date date) {
        return dateTimeFromOs(DateFormat.MEDIUM, DateFormat.SHORT, date);
    }

    public static String getDateWithFormat(@NonNull final DateTime time, @NonNull final String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHourOfDay());
        calendar.set(Calendar.MINUTE, time.getMinuteOfHour());
        SimpleDateFormat formatter = new SimpleDateFormat(format, getSupportedLocale());
        return formatter.format(calendar.getTime());
    }


    private static String datefromOs(int dateStyle, @NonNull final Date date) {
        return DateFormat.getDateInstance(dateStyle, getSupportedLocale()).format(date);
    }

    private static String timeFromOs(int timeStyle, @NonNull final Date date) {
        return DateFormat.getTimeInstance(timeStyle, getSupportedLocale()).format(date);
    }

    private static String dateTimeFromOs(int dateStyle, int timeStyle, @NonNull final Date date) {
        return DateFormat.getDateTimeInstance(dateStyle, timeStyle, getSupportedLocale()).format(date);
    }

    public static String getShortTimeFormatWithOptionalNewline() {
        final SimpleDateFormat instance = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        String pattern = instance.toPattern();
        if(hasAmPmMarker(pattern)){
            pattern = addNewline(pattern);
        }
        return pattern;
    }

    private static String addNewline(String pattern) {
        return pattern.replace("a", "\na");
    }

    private static boolean hasAmPmMarker(String pattern) {
        return pattern.contains("a");
    }

    public static String getFormattedMinuteAndSecond(int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat formatter = new SimpleDateFormat(FALLINGASLEEP_COUNTDOWN_FORMAT, getSupportedLocale());
        return formatter.format(calendar.getTime());
    }
}

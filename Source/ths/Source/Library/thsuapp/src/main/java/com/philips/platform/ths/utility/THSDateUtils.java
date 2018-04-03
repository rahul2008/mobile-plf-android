package com.philips.platform.ths.utility;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class THSDateUtils {

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static boolean isYearValid(Date year){
        int currentYear = Calendar.getInstance(Locale.US).get(Calendar.YEAR);
        int enteredYear = getCalendar(year).get(Calendar.YEAR);
        return enteredYear <= currentYear;
    }

    public static boolean isDateValid(Date date) {
        int currentDate = Calendar.getInstance(Locale.US).get(DATE);
        int enteredDate = getCalendar(date).get(DATE);
        return enteredDate <= currentDate;
    }
}

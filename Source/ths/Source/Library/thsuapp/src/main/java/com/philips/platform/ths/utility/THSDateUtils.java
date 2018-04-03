package com.philips.platform.ths.utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return enteredYear < currentYear;
    }

    public static boolean isDateValid(Date enteredDate) {
        String currentDateString;

        Date currentDate = new Date();
        SimpleDateFormat dates = new SimpleDateFormat("MMyyyy");

        //Setting dates
        currentDate.setTime(System.currentTimeMillis());
        currentDateString = dates.format(currentDate);
        try {
            currentDate = dates.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = enteredDate.getTime() - currentDate.getTime();
        return difference > 0;

    }
}

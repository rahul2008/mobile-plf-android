package com.philips.platform.appinfra.utility;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class AIUtilityTest {

    private String strDate = "1970-01-01 00:00:00.000 +0000";
    private Date date = new Date(0);
    private Date convertedDate;
    private String convertedString;

    @Test
    public void convertDateToStringTest() {
        whenConvertDateToStringIsInvoked();
        thenVerifyDateIsConvertedToString();
    }

    @Test
    public void convertStringToDateTest() {
        whenConvertStringToDateIsInvoked();
        thenVerifyStringIsConvertedToDate();
    }

    private void thenVerifyStringIsConvertedToDate() {
        assertEquals(date, convertedDate);
    }

    private void whenConvertStringToDateIsInvoked() {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS Z";
        convertedDate = AIUtility.convertStringToDate(strDate, pattern);
    }

    private void thenVerifyDateIsConvertedToString() {
        assertEquals(strDate, convertedString);
    }

    private void whenConvertDateToStringIsInvoked() {
        convertedString = AIUtility.convertDateToString(date);
    }
}
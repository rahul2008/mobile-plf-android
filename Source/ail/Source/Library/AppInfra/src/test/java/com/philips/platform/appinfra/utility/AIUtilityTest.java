package com.philips.platform.appinfra.utility;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class AIUtilityTest {

    private String strDate = "1970-01-01 00:00:00.000 +0000";

    //  "2018-07-30 06:29:05.717+0000"

    // "2018-04-04 08:39:30 +0000"


    private String strDate1 = "2018-07-30 06:29:05.717+0000";
    private String strDate2 = "2018-07-30 06:29:05 +0000";


    private String strDateToString1 = "2018-07-30 06:29:05.717 +0000"; // Taken hardcode value as converting date to string we convert to same format
    private String strDateToString2 = "2018-07-30 06:29:05.000 +0000";

    String pattern1 = "yyyy-MM-dd HH:mm:ss.SSSZ";
    String pattern2 = "yyyy-MM-dd HH:mm:ss Z";

    private Date date1 = AIUtility.convertStringToDate(strDate1,pattern1,pattern2);
    private Date date2 = AIUtility.convertStringToDate(strDate2,pattern2);
    private Date convertedDate;
    private String convertedString1;
    private String convertedString2;



    @Test
    public void convertDateToStringTestForStringDate1() {
        whenConvertDateToStringIsInvoked1();
        thenVerifyDateIsConvertedToString1();
    }

    @Test
    public void convertStringToDateTestForStringDate1() {
        whenConvertStringToDateIsInvoked(strDate1);
        thenVerifyStringIsConvertedToDate1();
    }


    @Test
    public void convertDateToStringTestForStringDate2() {
        whenConvertDateToStringIsInvoked2();
        thenVerifyDateIsConvertedToString2();
    }

    @Test
    public void convertStringToDateTestForStringDate2() {
        whenConvertStringToDateIsInvoked(strDate2);
        thenVerifyStringIsConvertedToDate2();
    }


    private void thenVerifyStringIsConvertedToDate1() {
        assertEquals(date1, convertedDate);
    }

    private void thenVerifyStringIsConvertedToDate2() {
        assertEquals(date2, convertedDate);
    }

    private void whenConvertStringToDateIsInvoked(String date) {
        convertedDate = AIUtility.convertStringToDate(date,pattern1,pattern2);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThroughRunTimeExceptionWhenPatternIsNotPassed() throws Exception {
        AIUtility.convertStringToDate(strDate1);
    }

    private void thenVerifyDateIsConvertedToString1() {
        assertEquals(strDateToString1, convertedString1);
    }

    private void whenConvertDateToStringIsInvoked1() {
        AIUtility.convertStringToDate(strDate1,pattern1);
        convertedString1 = AIUtility.convertDateToString(AIUtility.convertStringToDate(strDate1,pattern1,pattern2));
    }


    private void thenVerifyDateIsConvertedToString2() {
        assertEquals(strDateToString2, convertedString2);
    }

    private void whenConvertDateToStringIsInvoked2() {
        AIUtility.convertStringToDate(strDate2,pattern2);
        convertedString2 = AIUtility.convertDateToString(AIUtility.convertStringToDate(strDate2,pattern1,pattern2));
    }



}
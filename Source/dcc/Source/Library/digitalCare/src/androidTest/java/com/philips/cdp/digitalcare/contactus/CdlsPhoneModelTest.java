/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus;

import android.content.Context;

import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CdlsPhoneModelTest {

    private final String TAG = CdlsPhoneModelTest.class.getSimpleName();

    private Context context = null;

    private GetCdlsInstance mGetCdlsInstance = null;
    private CdlsResponseModel mCdlsResponseModel = null;

    @Before
    public void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();

        mGetCdlsInstance = new GetCdlsInstance(mParsingCompletedCallback);
    }

    @Test
    public void testContactUsWeekDaysBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        // mParser.processCdlsResponse(response);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = mCdlsObject.getOpeningHoursWeekdays();
        String expected = "Weekdays : Monday - Saturday: 9:00 AM - 9:00 PM EST";
        assertNotNull(received);
    }

    @Test
    public void testContactUsSaturdayDayBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = mCdlsObject.getOpeningHoursSaturday();
        assertNotNull(received);
    }

    @Test
    public void testContactUsSundayBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = mCdlsObject.getOpeningHoursSunday();
        String expected = "Sunday: 9:00 AM - 6:00 PM EST";
        assertNotNull(received);
    }

    @Test
    public void testContactUsOptionOneBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = mCdlsObject.getOptionalData1();
        String expected = "Excluding Major Holidays";

        assertNotNull(received);
    }

    @Test
    public void testContactUsOptionTwoBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = mCdlsObject.getOptionalData2();
        String expected = "For faster service, please have your product on-hand.";
        assertNotNull(received);

    }

    @Test
    public void testContactUsContactNumberBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = mCdlsObject.getPhoneNumber();
        String expected = "1-800-243-3050";
        assertEquals(expected, received);
    }

    @Test
    public void testContactUsWeekDaysBean1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = null;
        try {
            received = mCdlsObject.getOpeningHoursWeekdays();
        } catch (NullPointerException ignored) {

        } finally {
            assertNull(received);
        }
        /*
         * String expected =
         * "Weekdays : Monday - Saturday: 9:00 AM - 9:00 PM EST";
         * assertNotNull(received);
         */
    }

    @Test
    public void testContactUsSaturdayDayBean1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = null;
        try {
            received = mCdlsObject.getOpeningHoursSaturday();
        } catch (NullPointerException ignored) {

        } finally {
            assertNull(received);
        }
        // assertNotNull(received);
    }

    @Test
    public void testContactUsSundayBean1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = null;
        try {
            received = mCdlsObject.getOpeningHoursSunday();
        } catch (NullPointerException ignored) {

        } finally {
            assertNull(received);
        }
        /*
         * String expected = "Sunday: 9:00 AM - 6:00 PM EST";
         * assertNotNull(received);
         */
    }

    @Test
    public void testContactUsOptionOneBean1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = null;
        try {
            received = mCdlsObject.getOptionalData1();
        } catch (NullPointerException ignored) {

        } finally {
            assertNull(received);
        }
	/*	String expected = "Excluding Major Holidays";

		assertNotNull(received);*/
    }

    @Test
    public void testContactUsOptionTwoBean1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = null;
        try {
            received = mCdlsObject.getOptionalData2();
        } catch (NullPointerException ignored) {

        } finally {
            assertNull(received);
        }
        /*
         * String expected =
         * "For faster service, please have your product on-hand.";
         * assertNotNull(received);
         */
    }

    @Test
    public void testContactUsContactNumberBean1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
        String received = null;
        try {
            received = mCdlsObject.getPhoneNumber();
        } catch (NullPointerException ignored) {

        } finally {
            assertNull(received);
        }

        /*
         * String expected = "1-800-243-3050"; Log.d(TAG, "Naveen :" +
         * received); assertEquals(expected, received);
         */
    }

    private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            mCdlsResponseModel = response;

        }
    };
}

/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus;

import android.content.Context;

import com.philips.cdp.digitalcare.contactus.models.CdlsChatModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class CdlsChatModelTest {

    private final String TAG = CdlsChatModelTest.class.getSimpleName();

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
    public void testWeekDaysBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);

        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
        String received = mCdlsObject.getOpeningHoursWeekdays();

        assertNotNull(received);
    }

    @Test
    public void testSaturdayBean() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
        String received = mCdlsObject.getOpeningHoursSaturday();
        assertNotNull(received);
    }

    @Test
    public void testChatScriptContent() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
        String received = mCdlsObject.getContent();
        assertNotNull(received);
    }

    @Test
    public void testWeekDaysBean2() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
        String received = mCdlsObject.getOpeningHoursWeekdays();

        assertNotNull(received);
    }

    @Test
    public void testSaturdayBean2() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
        String received = mCdlsObject.getOpeningHoursSaturday();
        assertNotNull(received);
    }

    @Test
    public void testChatScriptContent2() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
        String received = mCdlsObject.getContent();
        assertNotNull(received);
    }

    @After
    protected void tearDown() throws Exception {
        DigiCareLogger.d(TAG, "tearDown..");
    }

    private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            mCdlsResponseModel = response;

        }
    };

}

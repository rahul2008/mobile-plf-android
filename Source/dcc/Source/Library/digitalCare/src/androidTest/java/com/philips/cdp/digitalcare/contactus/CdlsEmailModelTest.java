/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus;

import android.content.Context;

import com.philips.cdp.digitalcare.contactus.models.CdlsEmailModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CdlsEmailModelTest {

    private final String TAG = CdlsEmailModelTest.class.getSimpleName();

    private Context context = null;

    private GetCdlsInstance mGetCdlsInstance = null;
    private CdlsResponseModel mCdlsResponseModel = null;

    @Before
    protected void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();

        mGetCdlsInstance = new GetCdlsInstance(mParsingCompletedCallback);
    }

    @Test
    public void testEmailLabel() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();

        try {
            received = mCdlsObject.getLabel();
        } catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
        }
        assertNull(received);
    }

    @Test
    public void testEmailContent() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
        try {
            received = mCdlsObject.getContentPath().toString();
        } catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
        }

        assertNull(received);
    }

    @Test
    public void testEmailLabel1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls3.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
        try {
            received = mCdlsObject.getLabel();
        } catch (Exception ignored) {
        }
        assertNotNull(received);
    }

    @Test
    public void testEmailContent1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls3.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
        try {
            received = mCdlsObject.getContentPath();
        } catch (Exception ignored) {
        }
        assertNotNull(received);
    }

    @Test
    public void testEmailLabel2() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
        try {
            received = mCdlsObject.getLabel();
        } catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
        }
        assertNull(received);
    }

    @Test
    public void testEmailContent2() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        DigiCareLogger.d(TAG, "response emailcontent 2 : " + response);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
        try {
            received = mCdlsObject.getContentPath().toString();
        } catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
        }

        assertNull(received);
    }

    private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            mCdlsResponseModel = response;

        }
    };

}

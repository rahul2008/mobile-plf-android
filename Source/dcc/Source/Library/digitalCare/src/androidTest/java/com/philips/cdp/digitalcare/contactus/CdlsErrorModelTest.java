/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus;

import android.content.Context;

import com.philips.cdp.digitalcare.contactus.models.CdlsErrorModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNull;

public class CdlsErrorModelTest {

    private final String TAG = CdlsErrorModelTest.class.getSimpleName();

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
    public void testErrorCodeFromCdlsResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();
        try {
            received = mCdlsObject.getErrorCode();
        } catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
        }

        assertNull(received);
    }

    @Test
    public void testErrorMessageFromCdlsResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();

        try {
            received = mCdlsObject.getErrorMessage();
        } catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
        }

        assertNull(received);
    }

    @Test
    public void testErrorCodeFromCdlsResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();

        try {
            received = mCdlsObject.getErrorCode();
        } catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
        }

        assertNull(received);
    }

    @Test
    public void testErrorMessageFromCdlsResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        String received = null;
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();

        try {
            received = mCdlsObject.getErrorMessage();
        } catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
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

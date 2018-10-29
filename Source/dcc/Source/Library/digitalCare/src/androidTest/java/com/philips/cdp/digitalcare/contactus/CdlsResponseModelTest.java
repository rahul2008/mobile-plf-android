/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus;

import android.content.Context;

import com.philips.cdp.digitalcare.contactus.models.CdlsChatModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsEmailModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsErrorModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CdlsResponseModelTest {

    private final String TAG = CdlsResponseModelTest.class.getSimpleName();

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
    public void testChatBeanResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);

        CdlsChatModel mChatModel = mCdlsResponseModel.getChat();
        DigiCareLogger.d(TAG, "Chat Response : " + mChatModel);
        assertNotNull(mChatModel);
    }

    @Test
    public void testErrorBeanResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsErrorModel mErrorObject = mCdlsResponseModel.getError();
        DigiCareLogger.d(TAG, "Model Error : " + mErrorObject);
        assertNull(mErrorObject);
    }

    @Test
    public void testEmailBeanResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mEmailObject = mCdlsResponseModel.getEmail();
        assertNull(mEmailObject);
    }

    @Test
    public void testPhoneBeanResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mPhoneObject = mCdlsResponseModel.getPhone();
        DigiCareLogger.d(TAG, "Phone Response : " + mPhoneObject);
        assertNotNull(mPhoneObject);
    }

    @Test
    public void testJSONResponse() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        Boolean mResponseKey = mCdlsResponseModel.getSuccess();
        DigiCareLogger.d(TAG, " Response key : " + mResponseKey);
        assertTrue(mResponseKey);
    }

    @Test
    public void testResponseValidation1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        DigiCareLogger.d(TAG, "Response : " + mCdlsResponseModel);
        assertNotNull(mCdlsResponseModel);
    }

    @Test
    public void testChatBeanResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsChatModel mChatModel = mCdlsResponseModel.getChat();
        DigiCareLogger.d(TAG, "Chat Response : " + mChatModel);
        assertNotNull(mChatModel);
    }

    @Test
    public void testErrorBeanResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsErrorModel mErrorObject = mCdlsResponseModel.getError();
        DigiCareLogger.d(TAG, "Model Error : " + mErrorObject);
        assertNull(mErrorObject);
    }

    @Test
    public void testEmailBeanResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsEmailModel mEmailObject = mCdlsResponseModel.getEmail();
        assertNull(mEmailObject);
    }

    @Test
    public void testPhoneBeanResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        CdlsPhoneModel mPhoneObject = mCdlsResponseModel.getPhone();
        DigiCareLogger.d(TAG, "Phone Response : " + mPhoneObject);
        assertNull(mPhoneObject);
    }

    @Test
    public void testJSONResponse1() {
        String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
                context);
        mGetCdlsInstance.parseCdlsResponse(response);
        Boolean mResponseKey = mCdlsResponseModel.getSuccess();
        DigiCareLogger.d(TAG, " Response key : " + mResponseKey);
        assertTrue(mResponseKey);
    }

    private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            mCdlsResponseModel = response;
        }
    };
}

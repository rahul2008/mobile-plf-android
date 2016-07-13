package com.philips.cdp.prodreg.model.registerproduct;

import android.test.InstrumentationTestCase;

import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegistrationResponseTest extends InstrumentationTestCase {

    RegistrationResponse registrationResponse;
    @Mock
    RegistrationResponseData data;
    @Mock
    String mCTN, mSerialNumber, mAccessToken;
    RegistrationRequest mRegistrationRequest;
    private String TAG = getClass() + "";

    @Override
    public void setUp() throws Exception {
        registrationResponse = new RegistrationResponse();
        mRegistrationRequest = new RegistrationRequest(mCTN, mSerialNumber, mAccessToken);
    }

    public void testSetData() throws Exception {
        registrationResponse.setData(data);
    }

    public void testGetData() throws Exception {
        registrationResponse.setData(data);
        assertEquals(data, registrationResponse.getData());
    }

    @Test
    public void testSummaryResponseObject() {
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("product_registration.txt")));
                String mLine = reader.readLine();
                while (mLine != null) {
                    sb.append(mLine);
                    mLine = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RegistrationResponse registrationResponse = (RegistrationResponse) mRegistrationRequest.getResponseData(new JSONObject(sb.toString()));
            RegistrationResponseData mResponseData = registrationResponse.getData();
            assertNotNull(mResponseData);

            RegistrationResponseData data = setDataObject(mResponseData);

            TestAssertionOnResponse(mResponseData, data);
        } catch (Exception e) {
            ProdRegLogger.d(TAG, "IO " + e);
        }
    }

    @Test
    private void TestAssertionOnResponse(final RegistrationResponseData mResponseData, final RegistrationResponseData data) {
        assertEquals(mResponseData.getLocale(), data.getLocale());
        assertEquals(mResponseData.getModelNumber(), data.getModelNumber());
        assertEquals(mResponseData.getRegistrationDate(), data.getRegistrationDate());
        assertEquals(mResponseData.getDateOfPurchase(), data.getDateOfPurchase());
        assertEquals(mResponseData.getWarrantyEndDate(), data.getWarrantyEndDate());
        assertEquals(mResponseData.getContractNumber(), data.getContractNumber());
        assertEquals(mResponseData.getProductRegistrationUuid(), data.getProductRegistrationUuid());
        assertEquals(mResponseData.getEmailStatus(), data.getEmailStatus());
    }

    @Test
    private RegistrationResponseData setDataObject(final RegistrationResponseData mResponseData) {
        RegistrationResponseData data = new RegistrationResponseData();
        data.setLocale(mResponseData.getLocale());
        data.setModelNumber(mResponseData.getModelNumber());
        data.setRegistrationDate(mResponseData.getRegistrationDate());
        data.setDateOfPurchase(mResponseData.getDateOfPurchase());
        data.setWarrantyEndDate(mResponseData.getWarrantyEndDate());
        data.setContractNumber(mResponseData.getContractNumber());
        data.setProductRegistrationUuid(mResponseData.getProductRegistrationUuid());
        data.setEmailStatus(mResponseData.getEmailStatus());
        return data;
    }
}
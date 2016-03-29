package com.philips.cdp.model;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.prxrequest.RegistrationRequest;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegResponseTest extends InstrumentationTestCase {

    ProdRegResponse prodRegResponse;
    @Mock
    ProdRegData data;
    @Mock
    String mCTN, mSerialNumber, mAccessToken;
    RegistrationRequest mRegistrationRequest;
    private String TAG = getClass() + "";


    @Override
    public void setUp() throws Exception {
        prodRegResponse = new ProdRegResponse();
        mRegistrationRequest = new RegistrationRequest(mCTN, mSerialNumber, mAccessToken);
    }

    public void testSetData() throws Exception {
        prodRegResponse.setData(data);
    }

    public void testGetData() throws Exception {
        prodRegResponse.setData(data);
        assertEquals(data, prodRegResponse.getData());

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
            Log.d(TAG, "Parsed Data : " + sb.toString());

            ProdRegResponse prodRegResponse = (ProdRegResponse) mRegistrationRequest.getResponseData(new JSONObject(sb.toString()));
            ProdRegData mResponseData = prodRegResponse.getData();
            assertNotNull(mResponseData);

            ProdRegData data = setDataObject(mResponseData);

            TestAssertionOnResponse(mResponseData, data);
        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    @Test
    private void TestAssertionOnResponse(final ProdRegData mResponseData, final ProdRegData data) {
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
    private ProdRegData setDataObject(final ProdRegData mResponseData) {
        ProdRegData data = new ProdRegData();
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
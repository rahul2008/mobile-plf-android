package com.philips.cdp.model;

import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.productrequest.RegistrationRequest;

import org.json.JSONObject;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductResponseTest extends InstrumentationTestCase {
    @Mock
    String string1, string2, string3;
    RegistrationRequest mProductAssetBuilder = new RegistrationRequest(string1, string2, string3);
    private String TAG = getClass() + "";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

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

            ProductResponse productResponse = (ProductResponse) mProductAssetBuilder.getResponseData(new JSONObject(sb.toString()));
            Data mResponseData = productResponse.getData();
            assertNotNull(mResponseData);

            Data data = setDataObject(mResponseData);

            TestAssertionOnResponse(mResponseData, data);
        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    private void TestAssertionOnResponse(final Data mResponseData, final Data data) {
        assertEquals(mResponseData.getLocale(), data.getLocale());
        assertEquals(mResponseData.getModelNumber(), data.getModelNumber());
        assertEquals(mResponseData.getRegistrationDate(), data.getRegistrationDate());
        assertEquals(mResponseData.getDateOfPurchase(), data.getDateOfPurchase());
        assertEquals(mResponseData.getWarrantyEndDate(), data.getWarrantyEndDate());
        assertEquals(mResponseData.getContractNumber(), data.getContractNumber());
        assertEquals(mResponseData.getProductRegistrationUuid(), data.getProductRegistrationUuid());
        assertEquals(mResponseData.getEmailStatus(), data.getEmailStatus());
    }

    @NonNull
    private Data setDataObject(final Data mResponseData) {
        Data data = new Data();
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
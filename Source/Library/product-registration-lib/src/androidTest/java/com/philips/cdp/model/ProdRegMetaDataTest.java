package com.philips.cdp.model;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.google.gson.Gson;
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
public class ProdRegMetaDataTest extends InstrumentationTestCase {
    ProdRegMetaData prodRegMetaData;
    @Mock
    ProdRegMetaDataResponse data;
    @Mock
    String mCTN, mSerialNumber, mAccessToken;
    RegistrationRequest mRegistrationRequest;
    private String TAG = getClass() + "";

    @Override
    public void setUp() throws Exception {
        prodRegMetaData = new ProdRegMetaData();
        mRegistrationRequest = new RegistrationRequest(mCTN, mSerialNumber, mAccessToken);
    }
    public void testSetData() throws Exception {
        prodRegMetaData.setData(data);
    }
    public void testGetData() throws Exception {
        prodRegMetaData.setData(data);
        assertEquals(data, prodRegMetaData.getData());

    }
    @Test
    public void testSummaryResponseObject() {
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("product_metadata.txt")));
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

            ProdRegMetaData productMetaData = (ProdRegMetaData) mRegistrationRequest.getResponseData(new JSONObject(sb.toString()));
            ProdRegMetaDataResponse mResponseData = productMetaData.getData();
            assertNotNull(mResponseData);

            ProdRegMetaDataResponse productData = setMetadataObject(mResponseData);
            TestAssertionOnResponse(mResponseData, productData);
        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    @Test
    private void TestAssertionOnResponse(final ProdRegMetaDataResponse mResponseData, final ProdRegMetaDataResponse productData) {
        assertEquals(mResponseData.getMessage(), productData.getMessage());
        assertEquals(mResponseData.getIsConnectedDevice(), productData.getIsConnectedDevice());
        assertEquals(mResponseData.getRequiresDateOfPurchase(), productData.getRequiresDateOfPurchase());
        assertEquals(mResponseData.getExtendedWarrantyMonths(), productData.getExtendedWarrantyMonths());
        assertEquals(mResponseData.getHasGiftPack(), productData.getHasGiftPack());
        assertEquals(mResponseData.getSerialNumberFormat(), productData.getSerialNumberFormat());
        assertEquals(mResponseData.getRequiresDateOfPurchase(), productData.getRequiresDateOfPurchase());
        assertEquals(mResponseData.getSerialNumberSampleContent(), productData.getSerialNumberSampleContent());
    }

    @Test
    private ProdRegMetaDataResponse setMetadataObject(final ProdRegMetaDataResponse mResponseData) {
        ProdRegMetaDataResponse productData = new ProdRegMetaDataResponse();
        productData.setMessage(mResponseData.getMessage());
        productData.setIsConnectedDevice(mResponseData.getIsConnectedDevice());
        productData.setRequiresDateOfPurchase(mResponseData.getRequiresDateOfPurchase());
        productData.setExtendedWarrantyMonths(mResponseData.getExtendedWarrantyMonths());
        productData.setHasGiftPack(mResponseData.getHasGiftPack());
        productData.setSerialNumberFormat(mResponseData.getSerialNumberFormat());
        productData.setRequiresDateOfPurchase(mResponseData.getRequiresDateOfPurchase());
        productData.setSerialNumberSampleContent(mResponseData.getSerialNumberSampleContent());
        return productData;
    }


}
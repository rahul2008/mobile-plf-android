package com.philips.cdp.prodreg.model.metadata;

import android.test.InstrumentationTestCase;
import android.util.Log;

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
public class ProductMetadataResponseTest extends InstrumentationTestCase {
    ProductMetadataResponse productMetadataResponse;
    @Mock
    ProductMetadataResponseData data;
    @Mock
    String mCTN, mSerialNumber, mAccessToken;
    RegistrationRequest mRegistrationRequest;
    private String TAG = getClass() + "";

    @Override
    public void setUp() throws Exception {
        productMetadataResponse = new ProductMetadataResponse();
        mRegistrationRequest = new RegistrationRequest(mCTN, mSerialNumber, mAccessToken);
    }

    public void testSetData() throws Exception {
        productMetadataResponse.setData(data);
    }

    public void testGetData() throws Exception {
        productMetadataResponse.setData(data);
        assertEquals(data, productMetadataResponse.getData());
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

            ProductMetadataResponse productMetaData = (ProductMetadataResponse) mRegistrationRequest.getResponseData(new JSONObject(sb.toString()));
            ProductMetadataResponseData mResponseData = productMetaData.getData();
            assertNotNull(mResponseData);

            ProductMetadataResponseData productData = setMetadataObject(mResponseData);
            TestAssertionOnResponse(mResponseData, productData);
        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    @Test
    private void TestAssertionOnResponse(final ProductMetadataResponseData mResponseData, final ProductMetadataResponseData productData) {
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
    private ProductMetadataResponseData setMetadataObject(final ProductMetadataResponseData mResponseData) {
        ProductMetadataResponseData productData = new ProductMetadataResponseData();
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
package com.philips.cdp;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.model.ProdRegMetaData;
import com.philips.cdp.model.ProdRegMetaDataResponse;
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
public class ProductMetaDataResponseTest extends InstrumentationTestCase {
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

            ProdRegMetaData productMetaData = (ProdRegMetaData) mProductAssetBuilder.getResponseData(new JSONObject(sb.toString()));
            ProdRegMetaDataResponse mResponseData = productMetaData.getData();
            assertNotNull(mResponseData);

            ProdRegMetaDataResponse productData = setMetadataObject(mResponseData);
            TestAssertionOnResponse(mResponseData, productData);
        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

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
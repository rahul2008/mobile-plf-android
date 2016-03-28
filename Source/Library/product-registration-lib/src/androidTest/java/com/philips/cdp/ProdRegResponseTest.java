package com.philips.cdp;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.model.ProdRegData;
import com.philips.cdp.model.ProdRegResponse;
import com.philips.cdp.prxrequest.RegistrationRequest;

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
    @Mock
    String string1, string2, string3;
    RegistrationRequest mProductAssetBuilder;
    private String TAG = getClass() + "";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mProductAssetBuilder = new RegistrationRequest(string1, string2, string3);
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

            ProdRegResponse prodRegResponse = (ProdRegResponse) mProductAssetBuilder.getResponseData(new JSONObject(sb.toString()));
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
package com.philips.cdp.model;

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
public class ProductRegisteredResponseTest extends InstrumentationTestCase {
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("product_registered.txt")));
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

            RegisteredDataResponse registeredDataResponse = (RegisteredDataResponse) mProductAssetBuilder.getResponseData(new JSONObject(sb.toString()));
            Results[] mResponseData = registeredDataResponse.getResults();
            assertNotNull(mResponseData);

            Results[] setProductRegister = setProductRegister(mResponseData);
            TestAssertionOnResponse(mResponseData, setProductRegister);
        } catch (Exception e) {

            Log.d(TAG, "IO " + e);
        }
    }

    private void TestAssertionOnResponse(final Results[] mResponseData, final Results[] resultses) {

        assertEquals(mResponseData[0].getProductRegistrationID(), resultses[0].getProductRegistrationID());
        assertEquals(mResponseData[0].getPurchaseDate(), resultses[0].getPurchaseDate());
        assertEquals(mResponseData[0].getProductModelNumber(), resultses[0].getProductModelNumber());
        assertEquals(mResponseData[0].getContractNumber(), resultses[0].getContractNumber());
        assertEquals(mResponseData[0].getLastSolicitDate(), resultses[0].getLastSolicitDate());
        assertEquals(mResponseData[0].getPurchasePlace(), resultses[0].getPurchasePlace());
        assertEquals(mResponseData[0].getWarrantyInMonths(), resultses[0].getWarrantyInMonths());
        assertEquals(mResponseData[0].getId(), resultses[0].getId());
        assertEquals(mResponseData[0].getProductCatalogLocaleId(), resultses[0].getProductCatalogLocaleId());
        assertEquals(mResponseData[0].getDeviceId(), resultses[0].getDeviceId());
        assertEquals(mResponseData[0].getLastUpdated(), resultses[0].getLastUpdated());
        assertEquals(mResponseData[0].getIsPrimaryUser(), resultses[0].getIsPrimaryUser());
        assertEquals(mResponseData[0].getIsGenerations(), resultses[0].getIsGenerations());
        assertEquals(mResponseData[0].getDeviceName(), resultses[0].getDeviceName());
        assertEquals(mResponseData[0].getProductId(), resultses[0].getProductId());
        assertEquals(mResponseData[0].getExtendedWarranty(), resultses[0].getExtendedWarranty());
        assertEquals(mResponseData[0].getLastModified(), resultses[0].getLastModified());
        assertEquals(mResponseData[0].getSlashWinCompetition(), resultses[0].getSlashWinCompetition());
        assertEquals(mResponseData[0].getProductSerialNumber(), resultses[0].getProductSerialNumber());
        assertEquals(mResponseData[0].getCreated(), resultses[0].getCreated());
        assertEquals(mResponseData[0].getRegistrationDate(), resultses[0].getRegistrationDate());
        assertEquals(mResponseData[0].getUuid(), resultses[0].getUuid());
        assertEquals(mResponseData[0].getRegistrationChannel(), resultses[0].getRegistrationChannel());
    }

    private Results[] setProductRegister(final Results[] mResponseData) {

        Results[] pResults = new Results[0];
        pResults[0].setProductRegistrationID(mResponseData[0].getProductRegistrationID());
        pResults[0].setPurchaseDate(mResponseData[0].getPurchaseDate());
        pResults[0].setProductModelNumber(mResponseData[0].getProductModelNumber());
        pResults[0].setContractNumber(mResponseData[0].getContractNumber());
        pResults[0].setLastSolicitDate(mResponseData[0].getLastSolicitDate());
        pResults[0].setPurchasePlace(mResponseData[0].getPurchasePlace());
        pResults[0].setWarrantyInMonths(mResponseData[0].getWarrantyInMonths());
        pResults[0].setId(mResponseData[0].getId());
        pResults[0].setProductCatalogLocaleId(mResponseData[0].getProductCatalogLocaleId());
        pResults[0].setDeviceId(mResponseData[0].getDeviceId());
        pResults[0].setLastUpdated(mResponseData[0].getLastUpdated());
        pResults[0].setIsPrimaryUser(mResponseData[0].getIsPrimaryUser());
        pResults[0].setIsGenerations(mResponseData[0].getIsGenerations());
        pResults[0].setDeviceName(mResponseData[0].getDeviceName());
        pResults[0].setProductId(mResponseData[0].getProductId());
        pResults[0].setExtendedWarranty(mResponseData[0].getExtendedWarranty());
        pResults[0].setLastModified(mResponseData[0].getLastModified());
        pResults[0].setSlashWinCompetition(mResponseData[0].getSlashWinCompetition());
        pResults[0].setProductSerialNumber(mResponseData[0].getProductSerialNumber());
        pResults[0].setCreated(mResponseData[0].getCreated());
        pResults[0].setRegistrationDate(mResponseData[0].getRegistrationDate());
        pResults[0].setUuid(mResponseData[0].getUuid());
        pResults[0].setRegistrationChannel(mResponseData[0].getRegistrationChannel());

        return new Results[0];
    }
}

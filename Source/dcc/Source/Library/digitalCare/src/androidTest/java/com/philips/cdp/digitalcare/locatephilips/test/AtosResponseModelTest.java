/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.test;

import android.content.Context;

import com.philips.cdp.digitalcare.locatephilips.models.AtosErrorModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class AtosResponseModelTest {

    private final String TAG = AtosResponseModelTest.class.getSimpleName();
    private Context context = null;
    private GetAtosInstance atosInstance = null;

    @Before
    protected void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();
        atosInstance = new GetAtosInstance(mAtosParsing);
    }

    @Test
    public void testSuccess() {
        AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
        boolean recieved = atosResponseModel.getSuccess();

        assertTrue(recieved);
    }

    @Test
    public void testCdlsErrorModel() {
        AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
        AtosErrorModel mCdlsErrorModel = atosResponseModel.getCdlsErrorModel();

        assertNull(mCdlsErrorModel);
    }

    @Test
    public void testResultModel() {
        AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
        ArrayList<AtosResultsModel> mResultsModel = atosResponseModel
                .getResultsModel();

        assertNotNull(mResultsModel);
    }

    @Test
    public void testCurrentLocation() {
        AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
        AtosLocationModel mCurrentLocation = atosResponseModel
                .getCurrentLocation();

        assertNull(mCurrentLocation);
    }

    private AtosResponseModel atosResponseModel = null;

    private AtosResponseModel getAtosResultsModel(String jsonfile) {

        String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);

        atosInstance.parseAtosResponse(response);
        return atosResponseModel;
    }

    private AtosParsingCallback mAtosParsing = new AtosParsingCallback() {
        @Override
        public void onAtosParsingComplete(AtosResponseModel response) {
            atosResponseModel = response;
        }
    };

}

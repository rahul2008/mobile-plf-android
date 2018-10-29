/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.test;

import android.content.Context;

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
import static org.junit.Assert.assertTrue;

public class AtosLocationModelTest {

	private final String TAG = AtosLocationModelTest.class.getSimpleName();
	private Context context = null;
    private GetAtosInstance atosInstance = null;

    @Before
    protected void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();
        atosInstance = new GetAtosInstance(mAtosParsing);
    }

    @Test
    public void testLatitude() {
        AtosLocationModel atosLocationModel = getAtosLocationModel("atos.json");
        String received = atosLocationModel.getLatitude();
        assertNotNull(received);
    }

    @Test
    public void testwithoutLatitude() {
        AtosLocationModel atosLocationModel = getAtosLocationModel("atos_no_latitude.json");
        String received = atosLocationModel.getLatitude();
        assertTrue(received.isEmpty());
    }

    @Test
    public void testLongitude() {
        AtosLocationModel atosLocationModel = getAtosLocationModel("atos.json");
        String received = atosLocationModel.getLongitude();
        assertNotNull(received);
    }

    @Test
    public void testwithoutLongitude() {
        AtosLocationModel atosLocationModel = getAtosLocationModel("atos_no_longitude.json");
        String received = atosLocationModel.getLongitude();
        assertTrue(received.isEmpty());
    }

    private AtosLocationModel atosLocationModel = null;

	private AtosLocationModel getAtosLocationModel(String jsonfile) {
		String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);
		atosInstance.parseAtosResponse(response);
		return atosLocationModel;
	}

	private AtosParsingCallback mAtosParsing = new AtosParsingCallback() {
		@Override
		public void onAtosParsingComplete(AtosResponseModel atosResponseModel) {
			ArrayList<AtosResultsModel> resultList = atosResponseModel
					.getResultsModel();
			atosLocationModel = resultList.get(0).getLocationModel();
		}
	};
}

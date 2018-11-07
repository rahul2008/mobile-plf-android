/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.test;

import android.content.Context;

import com.philips.cdp.digitalcare.locatephilips.models.AtosAddressModel;
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


public class AtosResultsModelTest {

    private final String TAG = AtosResultsModelTest.class.getSimpleName();
    private Context context = null;

    private GetAtosInstance atosInstance = null;

    @Before
    public void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();
        atosInstance = new GetAtosInstance(mAtosParsing);
    }

    @Test
    public void testId() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
        for (int i = 0; i < resultList.size(); i++) {
            String received = resultList.get(i).getId();
            assertNotNull(received);
        }
    }

    @Test
    public void testwithoutId() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_id.json");
        for (int i = 0; i < resultList.size(); i++) {
            String received = resultList.get(i).getId();
            assertTrue(received.isEmpty());
        }
    }

    @Test
    public void testTitle() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
        for (int i = 0; i < resultList.size(); i++) {
            String received = resultList.get(i).getTitle();
            assertNotNull(received);
        }
    }

    @Test
    public void testwithoutTitle() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_title.json");
        for (int i = 0; i < resultList.size(); i++) {
            String received = resultList.get(i).getTitle();
            assertTrue(received.isEmpty());
        }
    }

    @Test
    public void testAddressModel() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
        for (int i = 0; i < resultList.size(); i++) {
            AtosAddressModel received = resultList.get(i).getAddressModel();
            assertNotNull(received);
        }
    }

    @Test
    public void testwithoutAddressModel() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_address.json");
        for (int i = 0; i < resultList.size(); i++) {
            AtosAddressModel received = resultList.get(i).getAddressModel();
            assertNull(received);
        }
    }

    @Test
    public void testAtosLocationModel() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
        for (int i = 0; i < resultList.size(); i++) {
            AtosLocationModel received = resultList.get(i).getLocationModel();
            assertNotNull(received);
        }
    }

    @Test
    public void testwithoutAtosLocationModel() {
        ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_location.json");
        for (int i = 0; i < resultList.size(); i++) {
            AtosLocationModel received = resultList.get(i).getLocationModel();
            assertNull(received);
        }
    }

    private ArrayList<AtosResultsModel> resultList = null;

    private ArrayList<AtosResultsModel> getResultModelList(String jsonfile) {
        String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);
        atosInstance.parseAtosResponse(response);
        return resultList;
    }

    private AtosParsingCallback mAtosParsing = new AtosParsingCallback() {
        @Override
        public void onAtosParsingComplete(AtosResponseModel atosResponseModel) {
            resultList = atosResponseModel.getResultsModel();
        }
    };

}

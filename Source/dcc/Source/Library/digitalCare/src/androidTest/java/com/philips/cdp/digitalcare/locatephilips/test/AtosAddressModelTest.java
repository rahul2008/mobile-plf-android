/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.test;

import android.content.Context;

import com.philips.cdp.digitalcare.locatephilips.models.AtosAddressModel;
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

public class AtosAddressModelTest {

    private final String TAG = AtosAddressModelTest.class.getSimpleName();
    private Context context = null;
    private GetAtosInstance atosInstance = null;

    @Before
    public void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();
        atosInstance = new GetAtosInstance(mAtosParsing);
    }

    @Test
    public void testZip() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        String received = atosAddressModel.getZip();
        assertNotNull(received);
    }

    @Test
    public void testwithoutZip() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos_no_zip.json");
        String received = atosAddressModel.getZip();
        assertTrue(received.isEmpty());
    }

    @Test
    public void testPhone() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        String received = atosAddressModel.getPhone();
        assertNotNull(received);
    }

    @Test
    public void testwithoutPhone() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos_no_phone.json");
        String received = atosAddressModel.getPhone();
        assertTrue(received.isEmpty());
    }

    @Test
    public void testAddress1() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        String received = atosAddressModel.getAddress1();
        assertNotNull(received);
    }

    @Test
    public void testAddress2() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        String received = atosAddressModel.getAddress2();
        assertNotNull(received);
    }

    @Test
    public void testUrl() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        String received = atosAddressModel.getUrl();
        assertNotNull(received);
    }

    @Test
    public void testCityState() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        String received = atosAddressModel.getCityState();
        assertNotNull(received);
    }

    @Test
    public void testCityState_withoutstate() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos_no_state.json");
        String received = atosAddressModel.getCityState();
        assertNotNull(received);
    }

    @Test
    public void testCityState_withoutcity() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos_no_city.json");
        String received = atosAddressModel.getCityState();
        assertNotNull(received);
    }

    @Test
    public void testPhoneList() {
        AtosAddressModel atosAddressModel = getAtosAddressModel("atos.json");
        ArrayList<String> received = atosAddressModel.getPhoneList();
        assertNotNull(received);
    }

    private AtosAddressModel atosAddressModel = null;

    private AtosAddressModel getAtosAddressModel(String jsonfile) {
        String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);
        atosInstance.parseAtosResponse(response);
        return atosAddressModel;
    }

    private AtosParsingCallback mAtosParsing = new AtosParsingCallback() {
        @Override
        public void onAtosParsingComplete(AtosResponseModel atosResponseModel) {
            ArrayList<AtosResultsModel> resultList = atosResponseModel
                    .getResultsModel();
            atosAddressModel = resultList.get(0).getAddressModel();
        }
    };

}

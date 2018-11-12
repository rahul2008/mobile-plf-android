/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.test;

import android.content.Context;

import com.philips.cdp.digitalcare.locatephilips.parser.MapDirectionsParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class MapDirectionsParserTest {

    private final String TAG = MapDirectionsParserTest.class.getSimpleName();
    private Context context = null;

    private MapDirectionsParser mMapDirectionParse;

    @Before
    public void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
        context = getInstrumentation().getContext();
        mMapDirectionParse = new MapDirectionsParser();
    }

    @Test
    public void testparse() {
        String jsonString = MapDirectionParserUtils.loadJSONFromAsset(
                "map_direction.json", context);
        List<List<HashMap<String, String>>> mMapList = null;
        try {
            JSONObject mapJson = new JSONObject(jsonString);
            mMapList = mMapDirectionParse.parse(mapJson);
            assertNotNull(mMapList);

        } catch (JSONException e) {
            e.printStackTrace();
            assertNotNull(mMapList);
        }

    }
}

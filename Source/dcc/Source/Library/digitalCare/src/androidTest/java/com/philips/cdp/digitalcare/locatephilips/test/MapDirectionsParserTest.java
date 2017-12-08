package com.philips.cdp.digitalcare.locatephilips.test;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.locatephilips.parser.MapDirectionsParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;


public class MapDirectionsParserTest extends InstrumentationTestCase {

	private final String TAG = MapDirectionsParserTest.class.getSimpleName();
	private Context context = null;

	MapDirectionsParser mMapDirectionParse;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
        DigiCareLogger.d(TAG, "setUp..");
		context = getInstrumentation().getContext();
		mMapDirectionParse = new MapDirectionsParser();
	}

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

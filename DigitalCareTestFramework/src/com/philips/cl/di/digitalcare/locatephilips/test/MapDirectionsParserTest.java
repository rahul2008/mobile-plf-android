package com.philips.cl.di.digitalcare.locatephilips.test;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cl.di.digitalcare.locatephilips.AtosResponseParser;
import com.philips.cl.di.digitalcare.locatephilips.MapDirectionsParser;

public class MapDirectionsParserTest extends InstrumentationTestCase {

	private final String TAG = MapDirectionsParserTest.class.getSimpleName();
	private Context mContext, context = null;
	private AtosResponseParser mParser = null;

	MapDirectionsParser mMapDirectionParse;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		mContext = getInstrumentation().getTargetContext();
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

package com.philips.cl.di.digitalcare.locatephilips.test;

import java.util.ArrayList;

import com.philips.cl.di.digitalcare.locatephilips.AtosAddressModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosLocationModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseParser;
import com.philips.cl.di.digitalcare.locatephilips.AtosResultsModel;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class AtosLocationModelTest extends InstrumentationTestCase {

	private final String TAG = AtosLocationModelTest.class.getSimpleName();
	private Context mContext, context = null;
	private AtosResponseParser mParser = null;

	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		mContext = getInstrumentation().getTargetContext();
		context = getInstrumentation().getContext();
		mParser = AtosResponseParser.getParserControllInstance(mContext);

	}

	public void testLatitude() {
		AtosLocationModel atosLocationModel = getAtosLocationModel("atos.json");
		String received = atosLocationModel.getLatitude();
		assertNotNull(received);
	}

	public void testwithoutLatitude() {
		AtosLocationModel atosLocationModel = getAtosLocationModel("atos_no_latitude.json");
		String received = atosLocationModel.getLatitude();
		// assertNotNull(received);
		assertTrue(received.isEmpty());
	}

	public void testLongitude() {
		AtosLocationModel atosLocationModel = getAtosLocationModel("atos.json");
		String received = atosLocationModel.getLongitude();
		assertNotNull(received);
	}

	public void testwithoutLongitude() {
		AtosLocationModel atosLocationModel = getAtosLocationModel("atos_no_longitude.json");
		String received = atosLocationModel.getLongitude();
		// assertNotNull(received);
		assertTrue(received.isEmpty());
	}

	private AtosLocationModel getAtosLocationModel(String jsonfile) {
		String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);
		mParser.processAtosResponse(response);
		AtosResponseModel atosResponseModel = mParser.getAtosResponse();
		ArrayList<AtosResultsModel> resultList = atosResponseModel
				.getResultsModel();
		AtosLocationModel atosLocationModel = resultList.get(0)
				.getLocationModel();
		return atosLocationModel;
	}
}

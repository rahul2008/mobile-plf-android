package com.philips.cl.di.digitalcare.locatephilips.test;

import java.util.ArrayList;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cl.di.digitalcare.locatephilips.AtosLocationModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosParsingCallback;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResultsModel;

public class AtosLocationModelTest extends InstrumentationTestCase {

	private final String TAG = AtosLocationModelTest.class.getSimpleName();
	private Context context = null;
	GetAtosInstance atosInstance = null;

	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		context = getInstrumentation().getContext();
		atosInstance = new GetAtosInstance(mAtosParsing);
	}

	public void testLatitude() {
		AtosLocationModel atosLocationModel = getAtosLocationModel("atos.json");
		String received = atosLocationModel.getLatitude();
		assertNotNull(received);
	}

	public void testwithoutLatitude() {
		AtosLocationModel atosLocationModel = getAtosLocationModel("atos_no_latitude.json");
		String received = atosLocationModel.getLatitude();
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
		assertTrue(received.isEmpty());
	}

	public AtosLocationModel atosLocationModel = null;

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

package com.philips.cl.di.digitalcare.locatephilips.test;

import java.util.ArrayList;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cl.di.digitalcare.locatephilips.AtosErrorModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosLocationModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseParser;
import com.philips.cl.di.digitalcare.locatephilips.AtosResultsModel;

public class AtosResponseModelTest extends InstrumentationTestCase {

	private final String TAG = AtosResponseModelTest.class.getSimpleName();
	private Context mContext, context = null;
	private AtosResponseParser mParser = null;

	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		mContext = getInstrumentation().getTargetContext();
		context = getInstrumentation().getContext();
		mParser = AtosResponseParser.getParserControllInstance(mContext);
	}

	public void testSuccess() {
		AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
		boolean recieved = atosResponseModel.getSuccess();

		assertTrue(recieved);
	}

	public void testCdlsErrorModel() {
		AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
		AtosErrorModel mCdlsErrorModel = atosResponseModel.getCdlsErrorModel();

		// object should be null for passing testcase
		assertNull(mCdlsErrorModel);

	}

	public void testResultModel() {
		AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
		ArrayList<AtosResultsModel> mResultsModel = atosResponseModel
				.getResultsModel();

		assertNotNull(mResultsModel);
	}

	public void testCurrentLocation() {
		AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
		AtosLocationModel mCurrentLocation = atosResponseModel
				.getCurrentLocation();

		assertNotNull(mCurrentLocation);
	}

	private AtosResponseModel getAtosResultsModel(String jsonfile) {
		String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);
		mParser.processAtosResponse(response);
		AtosResponseModel atosResponseModel = mParser.getAtosResponse();
		return atosResponseModel;
	}

}

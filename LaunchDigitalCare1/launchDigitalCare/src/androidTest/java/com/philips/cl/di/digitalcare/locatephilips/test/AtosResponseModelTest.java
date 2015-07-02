package com.philips.cl.di.digitalcare.locatephilips.test;

import java.util.ArrayList;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cl.di.digitalcare.locatephilips.AtosErrorModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosLocationModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosParsingCallback;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResultsModel;

public class AtosResponseModelTest extends InstrumentationTestCase {

	private final String TAG = AtosResponseModelTest.class.getSimpleName();
	private Context context = null;
	private GetAtosInstance atosInstance = null;

	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		context = getInstrumentation().getContext();
		atosInstance = new GetAtosInstance(mAtosParsing);
	}

	public void testSuccess() {
		AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
		boolean recieved = atosResponseModel.getSuccess();

		assertTrue(recieved);
	}

	public void testCdlsErrorModel() {
		AtosResponseModel atosResponseModel = getAtosResultsModel("atos.json");
		AtosErrorModel mCdlsErrorModel = atosResponseModel.getCdlsErrorModel();

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

	AtosResponseModel atosResponseModel = null;

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

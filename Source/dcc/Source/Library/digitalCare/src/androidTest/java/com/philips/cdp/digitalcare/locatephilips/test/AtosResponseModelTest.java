package com.philips.cdp.digitalcare.locatephilips.test;

import java.util.ArrayList;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.locatephilips.models.AtosErrorModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;


public class AtosResponseModelTest extends InstrumentationTestCase {

	private final String TAG = AtosResponseModelTest.class.getSimpleName();
	private Context context = null;
	private GetAtosInstance atosInstance = null;

	protected void setUp() throws Exception {
		super.setUp();
        DigiCareLogger.d(TAG, "setUp..");
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

		assertNull(mCurrentLocation);
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

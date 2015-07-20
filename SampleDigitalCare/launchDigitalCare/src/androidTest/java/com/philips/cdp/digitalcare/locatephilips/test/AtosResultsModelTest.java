package com.philips.cdp.digitalcare.locatephilips.test;

import java.util.ArrayList;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.locatephilips.AtosAddressModel;
import com.philips.cdp.digitalcare.locatephilips.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.AtosParsingCallback;
import com.philips.cdp.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.AtosResultsModel;

public class AtosResultsModelTest extends InstrumentationTestCase {

	private final String TAG = AtosResultsModelTest.class.getSimpleName();
	private Context context = null;

	private GetAtosInstance atosInstance = null;

	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		 context = getInstrumentation().getContext();
		 atosInstance = new GetAtosInstance(mAtosParsing);
	}

	public void testId() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
		for (int i = 0; i < resultList.size(); i++) {
			String received = resultList.get(i).getId();
			assertNotNull(received);
		}
	}

	public void testwithoutId() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_id.json");
		for (int i = 0; i < resultList.size(); i++) {
			String received = resultList.get(i).getId();
			assertTrue(received.isEmpty());
		}
	}

	public void testTitle() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
		for (int i = 0; i < resultList.size(); i++) {
			String received = resultList.get(i).getTitle();
			assertNotNull(received);
		}
	}

	public void testwithoutTitle() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_title.json");
		for (int i = 0; i < resultList.size(); i++) {
			String received = resultList.get(i).getTitle();
			assertTrue(received.isEmpty());
		}
	}

	public void testInfoType() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
		for (int i = 0; i < resultList.size(); i++) {
			String received = resultList.get(i).getInfoType();
			assertNotNull(received);
		}
	}

	public void testwithoutInfoType() {

		ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_infotype.json");
		for (int i = 0; i < resultList.size(); i++) {
			String received = resultList.get(i).getInfoType();
			assertTrue(received.isEmpty());
		}
	}

	public void testAddressModel() {

		ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
		for (int i = 0; i < resultList.size(); i++) {
			AtosAddressModel received = resultList.get(i).getAddressModel();
			assertNotNull(received);
		}
	}

	public void testwithoutAddressModel() {

		ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_address.json");
		for (int i = 0; i < resultList.size(); i++) {
			AtosAddressModel received = resultList.get(i).getAddressModel();
			assertNull(received);
		}
	}

	public void testAtosLocationModel() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos.json");
		for (int i = 0; i < resultList.size(); i++) {
			AtosLocationModel received = resultList.get(i).getLocationModel();
			assertNotNull(received);
		}
	}

	public void testwithoutAtosLocationModel() {
		ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_location.json");
		for (int i = 0; i < resultList.size(); i++) {
			AtosLocationModel received = resultList.get(i).getLocationModel();
			assertNull(received);
		}
	}

	public ArrayList<AtosResultsModel> resultList = null;

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

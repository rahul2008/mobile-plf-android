package com.philips.cl.di.digitalcare.locatephilips.test;

import java.util.ArrayList;

import org.mockito.Mockito;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cl.di.digitalcare.locatephilips.AtosAddressModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosLocationModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseParser;
import com.philips.cl.di.digitalcare.locatephilips.AtosResultsModel;
import com.philips.cl.di.digitalcare.util.DLog;

public class AtosResultsModelTest extends InstrumentationTestCase {

	private final String TAG = AtosResultsModelTest.class.getSimpleName();
	private Context mContext, context = null;
	private AtosResponseParser mParser = null;

	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");

		// mContext = getInstrumentation().getTargetContext();
		mContext = Mockito.mock(Context.class);
		context = getInstrumentation().getContext();
		mParser = AtosResponseParser.getParserControllInstance(mContext);

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
			AtosAddressModel received = resultList.get(i).getmAddressModel();
			assertNotNull(received);
		}
	}

	public void testwithoutAddressModel() {

		ArrayList<AtosResultsModel> resultList = getResultModelList("atos_no_address.json");
		for (int i = 0; i < resultList.size(); i++) {
			AtosAddressModel received = resultList.get(i).getmAddressModel();
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
			// assertNotNull(received);
			assertNull(received);
		}
	}

	private ArrayList<AtosResultsModel> getResultModelList(String jsonfile) {
		String response = AtosParserUtils.loadJSONFromAsset(jsonfile, context);
		mParser.processAtosResponse(response);
		AtosResponseModel atosResponseModel = mParser.getAtosResponse();
		ArrayList<AtosResultsModel> resultList = atosResponseModel
				.getResultsModel();
		return resultList;
	}

}

package com.philips.cl.di.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class CdlsErrorModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsErrorModelTest.class.getSimpleName();

	private Context mContext, context = null;
	private CdlsResponseParser mParser = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		mContext = getInstrumentation().getTargetContext();
		context = getInstrumentation().getContext();

		mParser = CdlsResponseParser.getParserControllInstance(mContext);
	}

	public void testErrorCodeFromCdlsResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mParser.getCdlsErrorModel();
		try {
			received = mCdlsObject.getErrorCode();
		} catch (NullPointerException e) {
			Log.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}

	public void testErrorMessageFromCdlsResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mParser.getCdlsErrorModel();
		try {
			received = mCdlsObject.getErrorMessage();
		} catch (NullPointerException e) {
			Log.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}
	
	public void testErrorCodeFromCdlsResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mParser.getCdlsErrorModel();
		try {
			received = mCdlsObject.getErrorCode();
		} catch (NullPointerException e) {
			Log.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}

	public void testErrorMessageFromCdlsResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mParser.getCdlsErrorModel();
		try {
			received = mCdlsObject.getErrorMessage();
		} catch (NullPointerException e) {
			Log.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}


}

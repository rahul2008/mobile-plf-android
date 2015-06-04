package com.philips.cl.di.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class CdlsEmailModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsEmailModelTest.class.getSimpleName();

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

	public void testEmailLabel() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mParser.getCdlsEmailModel();
		try {
			received = mCdlsObject.getContentPath().toString();
		} catch (Exception e) {
			Log.d(TAG, "Chat Email Content .." + received);
		}
		assertNull(received);
	}

	public void testEmailContent() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mParser.getCdlsEmailModel();
		try {
			received = mCdlsObject.getContentPath().toString();
		} catch (Exception e) {
			Log.d(TAG, "Chat Email Content .." + received);
		}

		assertNull(received);
	}

	public void testEmailLabel1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls3.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mParser.getCdlsEmailModel();
		Log.d("Naveen", "Chat Email Object .." + mCdlsObject);
		try {
			received = mCdlsObject.getContentPath().toString();
		} catch (Exception e) {
			Log.d("Naveen", "Chat Email Content .." + received);
		}
		assertNull(received);
	}

	public void testEmailContent1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls3.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mParser.getCdlsEmailModel();
		try {
			received = mCdlsObject.getContentPath().toString();
		} catch (Exception e) {
			// Log.d("Naveen", "Chat Email Content .." + received);
		}

		assertNull(received);
	}

}

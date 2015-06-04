package com.philips.cl.di.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class CdlsChatModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsChatModelTest.class.getSimpleName();

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

	public void testWeekDaysBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsChatModel mCdlsObject = mParser.getCdlsChatModel();
		String received = mCdlsObject.getOpeningHoursWeekdays();

		assertNotNull(received);
	}

	public void testSaturdayBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsChatModel mCdlsObject = mParser.getCdlsChatModel();
		String received = mCdlsObject.getOpeningHoursSaturday();
		assertNotNull(received);
	}

	public void testChatScriptContent() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsChatModel mCdlsObject = mParser.getCdlsChatModel();
		String received = mCdlsObject.getContent();
		assertNotNull(received);
	}
	
	public void testWeekDaysBean2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsChatModel mCdlsObject = mParser.getCdlsChatModel();
		String received = mCdlsObject.getOpeningHoursWeekdays();

		assertNotNull(received);
	}

	public void testSaturdayBean2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsChatModel mCdlsObject = mParser.getCdlsChatModel();
		String received = mCdlsObject.getOpeningHoursSaturday();
		assertNotNull(received);
	}

	public void testChatScriptContent2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsChatModel mCdlsObject = mParser.getCdlsChatModel();
		String received = mCdlsObject.getContent();
		assertNotNull(received);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Log.d(TAG, "tearDown..");
	}

}

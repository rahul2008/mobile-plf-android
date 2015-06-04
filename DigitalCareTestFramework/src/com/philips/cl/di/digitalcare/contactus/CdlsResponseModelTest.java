package com.philips.cl.di.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class CdlsResponseModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsResponseModelTest.class.getSimpleName();

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

	public void testResponseValidation() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		Log.d(TAG, "Response : " + mResponseModel);
		assertNotNull(mResponseModel);

	}

	public void testChatBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		CdlsChatModel mChatModel = mResponseModel.getChat();
		Log.d(TAG, "Chat Response : " + mChatModel);
		assertNotNull(mChatModel);
	}
	
	public void testErrorBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		CdlsErrorModel mErrorObject = mResponseModel.getError();
		Log.d(TAG, "Model Error : " + mErrorObject);
		assertNull(mErrorObject);
	}
	
	public void testEmailBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		CdlsEmailModel mEmailObject = mResponseModel.getEmail();
		Log.d("Naveen", "Response:"+ response);
		assertNull(mEmailObject);
	}
	
	
	public void testPhoneBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		CdlsPhoneModel mPhoneObject = mResponseModel.getPhone();
		Log.d(TAG, "Phone Response : " + mPhoneObject);
		assertNotNull(mPhoneObject);
	}
	
	public void testJSONResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		Boolean mResponseKey = mResponseModel.getSuccess();
		Log.d(TAG, " Response key : " + mResponseKey);
		assertTrue(mResponseKey);
	}
}

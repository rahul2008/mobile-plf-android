package com.philips.cdp.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.contactus.models.CdlsChatModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsEmailModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsErrorModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

public class CdlsResponseModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsResponseModelTest.class.getSimpleName();

	private Context mContext, context = null;
	private CdlsResponseParser mParser = null;

	GetCdlsInstance mGetCdlsInstance = null;
	CdlsResponseModel mCdlsResponseModel = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
        DigiCareLogger.d(TAG, "setUp..");
		mContext = getInstrumentation().getTargetContext();
		context = getInstrumentation().getContext();

		// mParser = CdlsResponseParser.getParserControllInstance(mContext);
		mGetCdlsInstance = new GetCdlsInstance(mParsingCompletedCallback);
	}

	// public void testResponseValidation() {
	// String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
	// context);
	// String received = null;
	// //mParser.processCdlsResponse(response);
	// mGetCdlsInstance.parseCdlsResponse(response);
	// CdlsResponseModel mResponseModel = null;
	// mResponseModel = mCdlsResponseModel.getCdlsResponseModel();
	// Log.d(TAG, "Response : " + mResponseModel);
	// assertNotNull(mResponseModel);
	//
	// }

	public void testChatBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);

		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsChatModel mChatModel = mCdlsResponseModel.getChat();
        DigiCareLogger.d(TAG, "Chat Response : " + mChatModel);
		assertNotNull(mChatModel);
	}

	public void testErrorBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsErrorModel mErrorObject = mCdlsResponseModel.getError();
        DigiCareLogger.d(TAG, "Model Error : " + mErrorObject);
		assertNull(mErrorObject);
	}

	public void testEmailBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsEmailModel mEmailObject = mCdlsResponseModel.getEmail();
		assertNull(mEmailObject);
	}

	public void testPhoneBeanResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsPhoneModel mPhoneObject = mCdlsResponseModel.getPhone();
        DigiCareLogger.d(TAG, "Phone Response : " + mPhoneObject);
		assertNotNull(mPhoneObject);
	}

	public void testJSONResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		Boolean mResponseKey = mCdlsResponseModel.getSuccess();
        DigiCareLogger.d(TAG, " Response key : " + mResponseKey);
		assertTrue(mResponseKey);
	}

	public void testResponseValidation1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
        DigiCareLogger.d(TAG, "Response : " + mCdlsResponseModel);
		assertNotNull(mCdlsResponseModel);

	}

	public void testChatBeanResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsChatModel mChatModel = mCdlsResponseModel.getChat();
        DigiCareLogger.d(TAG, "Chat Response : " + mChatModel);
		assertNotNull(mChatModel);
	}

	public void testErrorBeanResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsErrorModel mErrorObject = mCdlsResponseModel.getError();
        DigiCareLogger.d(TAG, "Model Error : " + mErrorObject);
		assertNull(mErrorObject);
	}

	public void testEmailBeanResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsEmailModel mEmailObject = mCdlsResponseModel.getEmail();
		assertNull(mEmailObject);
	}

	public void testPhoneBeanResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		CdlsPhoneModel mPhoneObject = mCdlsResponseModel.getPhone();
        DigiCareLogger.d(TAG, "Phone Response : " + mPhoneObject);
		assertNull(mPhoneObject);
	}

	public void testJSONResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		// CdlsResponseModel mResponseModel = null;
		// mResponseModel = mParser.getCdlsResponseModel();
		Boolean mResponseKey = mCdlsResponseModel.getSuccess();
        DigiCareLogger.d(TAG, " Response key : " + mResponseKey);
		assertTrue(mResponseKey);
	}

	private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
		@Override
		public void onCdlsParsingComplete(final CdlsResponseModel response) {
			mCdlsResponseModel = response;

		}
	};
}

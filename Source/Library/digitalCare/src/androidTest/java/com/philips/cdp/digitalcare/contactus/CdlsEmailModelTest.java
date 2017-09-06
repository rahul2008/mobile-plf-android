package com.philips.cdp.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.contactus.models.CdlsEmailModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

public class CdlsEmailModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsEmailModelTest.class.getSimpleName();

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

	public void testEmailLabel() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();

		try {
			received = mCdlsObject.getLabel();
		} catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
		}
		assertNull(received);
	}

	public void testEmailContent() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
		try {
			received = mCdlsObject.getContentPath().toString();
		} catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
		}

		assertNull(received);
	}

	public void testEmailLabel1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls3.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
		try {
			received = mCdlsObject.getLabel();
		} catch (Exception e) {
		}
		assertNotNull(received);
	}

	public void testEmailContent1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls3.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
		try {
			received = mCdlsObject.getContentPath();
		} catch (Exception e) {
		}
		assertNotNull(received);
	}

	public void testEmailLabel2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
		try {
			received = mCdlsObject.getLabel();
		} catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
		}
		assertNull(received);
	}

	public void testEmailContent2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
        DigiCareLogger.d(TAG, "response emailcontent 2 : " + response);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsEmailModel mCdlsObject = mCdlsResponseModel.getEmail();
		try {
			received = mCdlsObject.getContentPath().toString();
		} catch (Exception e) {
            DigiCareLogger.d(TAG, "Chat Email Content .." + received);
		}

		assertNull(received);
	}

	private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
		@Override
		public void onCdlsParsingComplete(final CdlsResponseModel response) {
			mCdlsResponseModel = response;

		}
	};

}

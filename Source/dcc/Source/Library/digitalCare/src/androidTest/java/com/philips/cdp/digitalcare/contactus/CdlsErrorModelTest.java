package com.philips.cdp.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.contactus.models.CdlsErrorModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

public class CdlsErrorModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsErrorModelTest.class.getSimpleName();

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

	public void testErrorCodeFromCdlsResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();
		try {
			received = mCdlsObject.getErrorCode();
		} catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}

	public void testErrorMessageFromCdlsResponse() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();
		
		try {
			received = mCdlsObject.getErrorMessage();
		} catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}

	public void testErrorCodeFromCdlsResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();
		
		try {
			received = mCdlsObject.getErrorCode();
		} catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
		}

		assertNull(received);
	}

	public void testErrorMessageFromCdlsResponse1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		String received = null;
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsErrorModel mCdlsObject = mCdlsResponseModel.getError();
		
		try {
			received = mCdlsObject.getErrorMessage();
		} catch (NullPointerException e) {
            DigiCareLogger.d(TAG, "Error Code response : " + received);
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

package com.philips.cdp.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.contactus.models.CdlsChatModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

public class CdlsChatModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsChatModelTest.class.getSimpleName();

	private Context mContext, context = null;

	// private CdlsResponseParser mParser = null;

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

	public void testWeekDaysBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);

		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
		String received = mCdlsObject.getOpeningHoursWeekdays();

		assertNotNull(received);
	}

	public void testSaturdayBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
		String received = mCdlsObject.getOpeningHoursSaturday();
		assertNotNull(received);
	}

	public void testChatScriptContent() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
		String received = mCdlsObject.getContent();
		assertNotNull(received);
	}

	public void testWeekDaysBean2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
		String received = mCdlsObject.getOpeningHoursWeekdays();

		assertNotNull(received);
	}

	public void testSaturdayBean2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
		String received = mCdlsObject.getOpeningHoursSaturday();
		assertNotNull(received);
	}

	public void testChatScriptContent2() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsChatModel mCdlsObject = mCdlsResponseModel.getChat();
		String received = mCdlsObject.getContent();
		assertNotNull(received);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
        DigiCareLogger.d(TAG, "tearDown..");
	}

	// private AtosParsingCallback mAtosParsing = new AtosParsingCallback() {
	//
	// @Override
	// public void onAtosParsingComplete(AtosResponseModel atosResponseModel) {
	//
	// }
	// };

	private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
		@Override
		public void onCdlsParsingComplete(final CdlsResponseModel response) {
			mCdlsResponseModel = response;

		}
	};

}

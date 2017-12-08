package com.philips.cdp.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

public class CdlsPhoneModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsPhoneModelTest.class.getSimpleName();

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

	public void testContactUsWeekDaysBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		// mParser.processCdlsResponse(response);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = mCdlsObject.getOpeningHoursWeekdays();
		String expected = "Weekdays : Monday - Saturday: 9:00 AM - 9:00 PM EST";
		assertNotNull(received);
	}

	public void testContactUsSaturdayDayBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = mCdlsObject.getOpeningHoursSaturday();
		assertNotNull(received);
	}

	public void testContactUsSundayBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = mCdlsObject.getOpeningHoursSunday();
		String expected = "Sunday: 9:00 AM - 6:00 PM EST";
		assertNotNull(received);
	}

	public void testContactUsOptionOneBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = mCdlsObject.getOptionalData1();
		String expected = "Excluding Major Holidays";

		assertNotNull(received);
	}

	public void testContactUsOptionTwoBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = mCdlsObject.getOptionalData2();
		String expected = "For faster service, please have your product on-hand.";
		assertNotNull(received);

	}

	public void testContactUsContactNumberBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = mCdlsObject.getPhoneNumber();
		String expected = "1-800-243-3050";
		assertEquals(expected, received);
	}

	public void testContactUsWeekDaysBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = null;
		try {
			received = mCdlsObject.getOpeningHoursWeekdays();
		} catch (NullPointerException e) {

		} finally {
			assertNull(received);
		}
		/*
		 * String expected =
		 * "Weekdays : Monday - Saturday: 9:00 AM - 9:00 PM EST";
		 * assertNotNull(received);
		 */
	}

	public void testContactUsSaturdayDayBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = null;
		try {
			received = mCdlsObject.getOpeningHoursSaturday();
		} catch (NullPointerException e) {

		} finally {
			assertNull(received);
		}
		// assertNotNull(received);
	}

	public void testContactUsSundayBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = null;
		try {
			received = mCdlsObject.getOpeningHoursSunday();
		} catch (NullPointerException e) {

		} finally {
			assertNull(received);
		}
		/*
		 * String expected = "Sunday: 9:00 AM - 6:00 PM EST";
		 * assertNotNull(received);
		 */
	}

	public void testContactUsOptionOneBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = null;
		try {
			received = mCdlsObject.getOptionalData1();
		} catch (NullPointerException e) {

		} finally {
			assertNull(received);
		}
	/*	String expected = "Excluding Major Holidays";

		assertNotNull(received);*/
	}

	public void testContactUsOptionTwoBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = null;
		try {
			received = mCdlsObject.getOptionalData2();
		} catch (NullPointerException e) {

		} finally {
			assertNull(received);
		}
		/*
		 * String expected =
		 * "For faster service, please have your product on-hand.";
		 * assertNotNull(received);
		 */
	}

	public void testContactUsContactNumberBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mGetCdlsInstance.parseCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mCdlsResponseModel.getPhone();
		String received = null;
		try {
			received = mCdlsObject.getPhoneNumber();
		} catch (NullPointerException e) {

		} finally {
			assertNull(received);
		}

		/*
		 * String expected = "1-800-243-3050"; Log.d(TAG, "Naveen :" +
		 * received); assertEquals(expected, received);
		 */
	}

	private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
		@Override
		public void onCdlsParsingComplete(final CdlsResponseModel response) {
			mCdlsResponseModel = response;

		}
	};
}

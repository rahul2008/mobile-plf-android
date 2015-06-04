package com.philips.cl.di.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cl.di.digitalcare.util.DLog;

public class CdlsPhoneModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsPhoneModelTest.class.getSimpleName();

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

	public void testContactUsWeekDaysBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOpeningHoursWeekdays();
		String expected = "Weekdays : Monday - Saturday: 9:00 AM - 9:00 PM EST";
		DLog.d(TAG, "Weekdays Bean : [" + received + "]");
		assertNotNull(received);
	}

	public void testContactUsSaturdayDayBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOpeningHoursSaturday();
		Log.d(TAG, "Saturday :" + received);
		assertNotNull(received);
	}

	public void testContactUsSundayBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOpeningHoursSunday();
		String expected = "Sunday: 9:00 AM - 6:00 PM EST";
		assertNotNull(received);
		Log.d(TAG, "Sunday :" + received);
	}

	public void testContactUsOptionOneBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOptionalData1();
		String expected = "Excluding Major Holidays";

		assertNotNull(received);
	}

	public void testContactUsOptionTwoBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOptionalData2();
		String expected = "For faster service, please have your product on-hand.";
		assertNotNull(received);
		Log.d(TAG, "Option2 :" + received);
	}

	public void testContactUsContactNumberBean() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getPhoneNumber();
		String expected = "1-800-243-3050";
		assertEquals(expected, received);
		Log.d(TAG, "PhoneNumber :" + received);
	}
	public void testContactUsWeekDaysBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOpeningHoursWeekdays();
		String expected = "Weekdays : Monday - Saturday: 9:00 AM - 9:00 PM EST";
		DLog.d(TAG, "Weekdays Bean : [" + received + "]");
		assertNotNull(received);
	}

	public void testContactUsSaturdayDayBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOpeningHoursSaturday();
		Log.d(TAG, "Saturday :" + received);
		assertNotNull(received);
	}

	public void testContactUsSundayBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOpeningHoursSunday();
		String expected = "Sunday: 9:00 AM - 6:00 PM EST";
		assertNotNull(received);
		Log.d(TAG, "Sunday :" + received);
	}

	public void testContactUsOptionOneBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOptionalData1();
		String expected = "Excluding Major Holidays";

		assertNotNull(received);
	}

	public void testContactUsOptionTwoBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getOptionalData2();
		String expected = "For faster service, please have your product on-hand.";
		assertNotNull(received);
		Log.d(TAG, "Option2 :" + received);
	}

	public void testContactUsContactNumberBean1() {
		String response = CdlsParserUtils.loadJSONFromAsset("cdls2.json",
				context);
		mParser.processCdlsResponse(response);
		CdlsPhoneModel mCdlsObject = mParser.getCdlsPhoneModel();
		String received = mCdlsObject.getPhoneNumber();
		String expected = "1-800-243-3050";
		assertEquals(expected, received);
		Log.d(TAG, "PhoneNumber :" + received);
	}

}

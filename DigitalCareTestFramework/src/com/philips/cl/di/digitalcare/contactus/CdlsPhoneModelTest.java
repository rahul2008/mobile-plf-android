package com.philips.cl.di.digitalcare.contactus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.contactus.CdlsPhoneModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;


/**
 * 
 * @author naveen@philips.com
 *
 */
public class CdlsPhoneModelTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	/*
	 * "phone":[ { "phoneNumber":"1-800-243-3050", "phoneTariff":"",
	 * "openingHoursWeekdays":"Monday - Saturday: 9:00 AM - 9:00 PM EST",
	 * "openingHoursSaturday":"",
	 * "openingHoursSunday":"Sunday: 9:00 AM - 6:00 PM EST",
	 * "optionalData1":"Excluding Major Holidays",
	 * "optionalData2":"For faster service, please have your product on-hand." }
	 * ],
	 */

	CdlsPhoneModel fixture = null;
	private String mPhoneNumber = "1-800-243-3050";
	private String mOpeningHoursWeekdays = "Monday - Saturday: 9:00 AM - 9:00 PM EST";
	private String mOpeningHoursSaturday = "Monday - Saturday: 9:00 AM - 9:00 PM EST";
	private String mOpeningHoursSunday = "Sunday: 9:00 AM - 6:00 PM EST";

	public CdlsPhoneModelTest() {
		super(LaunchDigitalCare.class);
	}

	@Test
	public void testPhoneNumberMethod() throws Exception {
		String result = fixture.getPhoneNumber();
		assertEquals(mPhoneNumber, result);
	}
//
//	@Test
//	public void testSaturdayOpenHourMethod() throws Exception {
//		String result = fixture.getOpeningHoursSaturday();
//		assertEquals(mOpeningHoursSaturday, result);
//	}
//
//	@Test
//	public void testSundayOpenHourMethod() throws Exception {
//		String result = fixture.getOpeningHoursSunday();
//		assertEquals(mOpeningHoursSunday, result);
//	}
//
//	@Test
//	public void testWeekdayOpenHourMethod() throws Exception {
//		String result = fixture.getOpeningHoursWeekdays();
//		assertEquals(mOpeningHoursWeekdays, result);
//	}

	@Before
	public void setUp() throws Exception {

		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		fixture = new CdlsPhoneModel();
		fixture.setPhoneNumber(mPhoneNumber);
		fixture.setOpeningHoursWeekdays(mOpeningHoursWeekdays);
		fixture.setOpeningHoursSaturday(mOpeningHoursSaturday);
		fixture.setOpeningHoursSunday(mOpeningHoursSunday);
	}

	@After
	public void tearDown() throws Exception {
	}

}
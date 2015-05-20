package com.philips.cl.di.digitalcare.bean.test;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.contactus.CdlsChatModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsChatModelTest extends
ActivityInstrumentationTestCase2<LaunchDigitalCare> {
	
	
	/*"phone":[
	{
	"phoneNumber":"1-800-243-3050",
	"phoneTariff":"",
	"openingHoursWeekdays":"Monday - Saturday: 9:00 AM - 9:00 PM EST",
	"openingHoursSaturday":"",
	"openingHoursSunday":"Sunday: 9:00 AM - 6:00 PM EST",
	"optionalData1":"Excluding Major Holidays",
	"optionalData2":"For faster service, please have your product on-hand."
	}
	],*/
	
	private final String mContent = "For faster service, please have your product on-hand.";
	private final String mSaturdayOpeningHours = "Sunday: 9:00 AM - 6:00 PM EST";
	private final String mWeekdaysOpeningHours = "Monday - Saturday: 9:00 AM - 9:00 PM EST";
	
	CdlsChatModel fixture = null;
	

	public CdlsChatModelTest() {
		super(LaunchDigitalCare.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		fixture = new CdlsChatModel();
		fixture.setContent(mContent);
		fixture.setOpeningHoursSaturday(mSaturdayOpeningHours);
		fixture.setOpeningHoursWeekdays(mWeekdaysOpeningHours);
		
	}
	
	@Test
	public void testContent()
		throws Exception {

		String result = fixture.getContent();
		assertEquals(mContent, result);
	}
	

}

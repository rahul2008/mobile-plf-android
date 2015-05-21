package com.philips.cl.di.digitalcare.contactus;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.contactus.CdlsChatModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;


/**
 * 
 * @author naveen@philips.com
 *
 */
public class CdlsChatModelTest extends
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

	private final String mContent = " \u003c!-- BEGIN LIVECOM CHAT LINK SCRIPT --\u003e\n\u003cspan class\u003d\"lc5element\" onclick\u003d\"omnitureTrackClick(\u0027chat\u0027)\"\u003e\n \u003c!-- lcType\u003d\"link\" \n lcGroup\u003d\"2\" \n lcChan\u003d\"LWC;LVC;LVI\" \n lcId\u003d\"Product_US_en\" \n lcTag\u003d\"PERSONAL_CARE_GR\" \n lcOnval\u003d\"Click for Live Chat!\" \n lcOffval\u003d\"Click for Live Chat!\" \n lcCustomattributes\u003d\"Group:PERSONAL_CARE_GR; \n Category:MENS_SHAVING_CA; \n Sub-category:PARAM_SUBCAT; \n CTN:PARAM_CTN; \n Country:US; \n Language:en\"\n --\u003e\n\u003c/span\u003e\n\u003c!-- END LIVECOM CHAT LINK SCRIPT --\u003e"
			+ "script"
			+ "\u003c!-- BEGIN LIVECOM COMMUNICATION SCRIPT --\u003e\n\u003cspan class\u003d\"lc5element\"\u003e\u003c!-- lcType\u003d\"page\" lcAccount\u003d\"kplNOOsAAAA\u003d\" lcTag\u003d\"PERSONAL_CARE_GR\" --\u003e\u003c/span\u003e\n\u003cspan class\u003d\"lc5element\"\u003e\u003c!-- lcType\u003d\"mod\" lcKey\u003d\"LTR\" lcGroup\u003d\"2\" --\u003e\u003c/span\u003e\n\u003cdiv style\u003d\"display: none;\" id\u003d\"LCcopyright\"\u003e\u003ca href\u003d\"http://www.livecom.net\"\u003elivecom.net chat\u003c/a\u003e\u003c/div\u003e\n\u003cscript charset\u003d\"ISO-8859-1\" type\u003d\"text/javascript\" src\u003d\"//ph-us.livecom.net/5g/ws/kplNOOsAAAA\u003d\"\u003e\u003c/script\u003e\n\u003c!-- END LIVECOM COMMUNICATION SCRIPT --\u003e";
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
	public void testContentLogic() throws Exception {

		String result = fixture.getContent();
		assertEquals(mContent, result);
	}

	@Test
	public void testOpeningHoursOnWeekdaysLogic() throws Exception {

		String result = fixture.getOpeningHoursWeekdays();
		assertEquals(mWeekdaysOpeningHours, result);
	}

	@Test
	public void testOpeningHoursOnWeekEndLogic() throws Exception {

		String result = fixture.getOpeningHoursSaturday();
		assertEquals(mSaturdayOpeningHours, result);
	}

	/*
	 * @Test public void testContentWithMock() { DLog.e(TAG,
	 * "Mocked Object init : "); CdlsResponseCallback mChatModel =
	 * EasyMock.createNiceMock(CdlsResponseCallback.class); DLog.e(TAG,
	 * "Mocked Object : "+ mChatModel.onCdlsResponseReceived(response)); }
	 */

}

package com.philips.cl.di.digitalcare.contactus;

import org.junit.After;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.contactus.CdlsChatModel;
import com.philips.cl.di.digitalcare.contactus.CdlsEmailModel;
import com.philips.cl.di.digitalcare.contactus.CdlsErrorModel;
import com.philips.cl.di.digitalcare.contactus.CdlsPhoneModel;
import com.philips.cl.di.digitalcare.contactus.CdlsResponseModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

/**
 * 
 * @author naveen@philips.com
 * 
 */
public class CdlsResponseModelTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	private CdlsPhoneModel phone = null;
	private CdlsChatModel chat = null;
	private CdlsEmailModel email = null;
	private CdlsErrorModel error = null;
	private boolean success = true;
	public CdlsResponseModelTest() {
		super(LaunchDigitalCare.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		phone = new CdlsPhoneModel();
		chat = new CdlsChatModel();
		email = new CdlsEmailModel();
		error = new CdlsErrorModel();

	}

	@Test
	public void testCdlsResponseModel() throws Exception {
		
		CdlsResponseModel result = new CdlsResponseModel(success, phone, chat,
				email, error);

		assertNotNull(result);
		assertEquals(true, result.getSuccess());
	}

	@Test
	public void testGetChat_1() throws Exception {
		CdlsResponseModel fixture = new CdlsResponseModel(true,
				new CdlsPhoneModel(), new CdlsChatModel(),
				new CdlsEmailModel(), new CdlsErrorModel());

		CdlsChatModel result = fixture.getChat();

		// add additional test code here
		assertEquals(null, result.getContent());
		assertEquals(null, result.getOpeningHoursSaturday());
		assertEquals(null, result.getOpeningHoursWeekdays());
	}

	@Test
	public void testGetEmail() throws Exception {
		CdlsResponseModel fixture = new CdlsResponseModel(true,
				new CdlsPhoneModel(), new CdlsChatModel(),
				new CdlsEmailModel(), new CdlsErrorModel());

		CdlsEmailModel result = fixture.getEmail();

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getLabel());
		assertEquals(null, result.getContentPath());
	}

	@Test
	public void testGetError() throws Exception {
		CdlsResponseModel fixture = new CdlsResponseModel(true,
				new CdlsPhoneModel(), new CdlsChatModel(),
				new CdlsEmailModel(), new CdlsErrorModel());

		CdlsErrorModel result = fixture.getError();

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getErrorMessage());
		assertEquals(null, result.getErrorCode());
	}

//	@Test
//	public void testGetPhone() throws Exception {
//		CdlsResponseModel fixture = new CdlsResponseModel(true,
//				new CdlsPhoneModel(), new CdlsChatModel(),
//				new CdlsEmailModel(), new CdlsErrorModel());
//
//		CdlsPhoneModel result = fixture.getPhone();
//
//		// add additional test code here
//		assertNotNull(result);
//		assertEquals(null, result.getOpeningHoursSaturday());
//		assertEquals(null, result.getOpeningHoursSunday());
//		assertEquals(null, result.getOpeningHoursWeekdays());
//		assertEquals(null, result.getPhoneNumber());
//	}

	@Test
	public void testGetSuccess() throws Exception {
		CdlsResponseModel fixture = new CdlsResponseModel(true,
				new CdlsPhoneModel(), new CdlsChatModel(),
				new CdlsEmailModel(), new CdlsErrorModel());

		boolean result = fixture.getSuccess();

		assertEquals(true, result);
	}

	@After
	public void tearDown() throws Exception {
	}

}
package com.philips.cl.di.digitalcare.bean.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.contactus.CdlsEmailModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

/**
 * 
 * @author naveen@philips.com
 * 
 */
public class CdlsEmailModelTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	CdlsEmailModel fixture = null;
	private final String EMAIL = "philips@philips.com";
	private final String DESCRIPTION = "Content description sends to Philips Support Team";

	public CdlsEmailModelTest() {
		super(LaunchDigitalCare.class);
	}

	@Test
	public void testEmailLogic() throws Exception {
		String result = fixture.getLabel();
		assertEquals(EMAIL, result);
	}

	@Test
	public void testEmailContentLogic() throws Exception {
		String result = fixture.getContentPath();
		assertEquals(DESCRIPTION, result);
	}

	@Before
	public void setUp() throws Exception {

		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		fixture = new CdlsEmailModel();
		fixture.setLabel(EMAIL);
		fixture.setContentPath(DESCRIPTION);
	}

	@After
	public void tearDown() throws Exception {
	}

}
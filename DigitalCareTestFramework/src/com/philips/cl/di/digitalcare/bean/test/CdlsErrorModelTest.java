package com.philips.cl.di.digitalcare.bean.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.contactus.CdlsErrorModel;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

/**
 * 
 * @author naveen@philips.com
 *
 */
public class CdlsErrorModelTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	CdlsErrorModel fixture = null;
	private final String ERROR_CODE = "501";
	private final String ERROR_MESSAGE = "Web server out of date";

	public CdlsErrorModelTest() {
		super(LaunchDigitalCare.class);
	}

	@Test
	public void testErrorCodeLogic() throws Exception {
		String result = fixture.getErrorCode();
		assertEquals(ERROR_CODE, result);
	}

	@Test
	public void testErrorMessageLogic() throws Exception {
		String result = fixture.getErrorMessage();
		assertEquals(ERROR_MESSAGE, result);
	}

	@Before
	public void setUp() throws Exception {

		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		fixture = new CdlsErrorModel();
		fixture.setErrorCode(ERROR_CODE);
		fixture.setErrorMessage(ERROR_MESSAGE);
	}

	@After
	public void tearDown() throws Exception {
	}

}
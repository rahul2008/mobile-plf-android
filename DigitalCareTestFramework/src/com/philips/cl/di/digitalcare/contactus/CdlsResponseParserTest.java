package com.philips.cl.di.digitalcare.contactus;

import org.junit.After;
import org.junit.Test;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.MutableContextWrapper;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ContextThemeWrapper;

import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

/**
 * 
 * @author naveen@philips.com
 * 
 */
public class CdlsResponseParserTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public CdlsResponseParserTest() {
		super(LaunchDigitalCare.class);
	}

	/**
	 * Without Initialization trying to get The CdlsBean. It should return Null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetCdlsBean() throws Exception {
		CdlsResponseParser fixture = CdlsResponseParser
				.getParserControllInstance(new ContextWrapper(
						new MutableContextWrapper(new ContextThemeWrapper())));

		CdlsResponseModel result = fixture.getCdlsBean();

		assertNull(result);
	}

	@Test
	public void testGetParserInstance() throws Exception {
		Context context = new ContextWrapper(new MutableContextWrapper(
				new ContextThemeWrapper()));

		CdlsResponseParser result = CdlsResponseParser
				.getParserControllInstance(context);

		assertNotNull(result);
	}

	/**
	 * Needs to improve this testCase
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProcessCdlsResponse() throws Exception {
		CdlsResponseParser fixture = CdlsResponseParser
				.getParserControllInstance(new ContextWrapper(
						new MutableContextWrapper(new ContextThemeWrapper())));
		String response = "dapa";

		fixture.processCdlsResponse(response);

	}

	@After
	public void tearDown() throws Exception {
	}

}
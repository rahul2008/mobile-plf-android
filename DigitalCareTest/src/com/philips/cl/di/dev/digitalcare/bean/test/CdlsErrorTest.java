package com.philips.cl.di.dev.digitalcare.bean.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.DigitalCareActivity;
import com.philips.cl.di.digitalcare.contactus.CdlsError;


/**
 * 
 * @author naveen@philips.com
 * @description Testing CdlsError Bean class using MOCKITO. 
 * @Since  Mar 12, 2015
 */
public class CdlsErrorTest extends
		ActivityInstrumentationTestCase2<DigitalCareActivity> {

	private CdlsError mObject = null;
	private final String ERROR_MESSAGE1 = "null";
	private final String ERROR_MESSAGE2 = "print_stractracce";
	private final String ERROR_CODE1 = "1288383";
	private final String ERROR_CODE2 = "401";

	public CdlsErrorTest() {
		super(DigitalCareActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mObject = mock(CdlsError.class);
	}

	@SmallTest
	public void testErrorMessageReceivedCycle1() {
		when(mObject.getErrorMessage()).thenReturn(ERROR_MESSAGE1);
		assertEquals(ERROR_MESSAGE1, mObject.getErrorMessage());
	}

	@SmallTest
	public void testErrorMessageReceivedCycle2() {
		when(mObject.getErrorMessage()).thenReturn(ERROR_MESSAGE2);
		assertEquals(ERROR_MESSAGE2, mObject.getErrorMessage());
	}

	@SmallTest
	public void testErrorCodeReceivedCycle1() {

		when(mObject.getErrorCode()).thenReturn(ERROR_CODE1);
		assertEquals(ERROR_CODE1, mObject.getErrorCode());
	}

	@SmallTest
	public void testErrorCodeReceivedCycle2() {
		when(mObject.getErrorCode()).thenReturn(ERROR_CODE2);
		assertEquals(ERROR_CODE2, mObject.getErrorCode());
	}
}

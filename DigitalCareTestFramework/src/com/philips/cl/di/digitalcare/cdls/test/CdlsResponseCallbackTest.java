package com.philips.cl.di.digitalcare.cdls.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.contactus.CdlsResponseCallback;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class CdlsResponseCallbackTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public CdlsResponseCallbackTest() {
		super(LaunchDigitalCare.class);
	}

	private CdlsResponseCallback mCdlsResponseCallback = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mCdlsResponseCallback = mock(CdlsResponseCallback.class);
	}

	@SmallTest
	public void isCdlsResponseCallbackIsMocked() {
		boolean validate = false;
		String received = mCdlsResponseCallback.getClass().getSimpleName();
		if (received.equalsIgnoreCase("CdlsResponseCallback_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

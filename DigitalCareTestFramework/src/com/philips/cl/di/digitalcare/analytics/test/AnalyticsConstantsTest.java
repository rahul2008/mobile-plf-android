package com.philips.cl.di.digitalcare.analytics.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class AnalyticsConstantsTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public AnalyticsConstantsTest() {
		super(LaunchDigitalCare.class);
	}

	private AnalyticsConstants mAnalyticsConstantsClass = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mAnalyticsConstantsClass = mock(AnalyticsConstants.class);
	}

	@SmallTest
	public void testIsAnalyticsConstantsIsMocked() {
		boolean validate = false;
		DLog.d("Naveen", "Class Name : "+ mAnalyticsConstantsClass.getClass().getSimpleName());
		String received = mAnalyticsConstantsClass.getClass().getSimpleName();
		if (received.equalsIgnoreCase("AnalyticsConstants_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

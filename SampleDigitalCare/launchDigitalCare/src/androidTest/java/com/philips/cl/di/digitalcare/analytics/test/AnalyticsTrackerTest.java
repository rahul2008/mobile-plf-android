package com.philips.cl.di.digitalcare.analytics.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class AnalyticsTrackerTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public AnalyticsTrackerTest() {
		super(LaunchDigitalCare.class);
	}
	
	private AnalyticsTracker mAnalyticsTrackerClass = null;
	
	@Override
	protected void setUp() throws Exception {
	super.setUp();
	System.setProperty("dexmaker.dexcache", getInstrumentation()
			.getTargetContext().getCacheDir().getPath());
	mAnalyticsTrackerClass = mock(AnalyticsTracker.class);
	}

	@SmallTest
	public void testIsAnalyticsTrackerIsMocked() {
	boolean validate = false;
	String received = mAnalyticsTrackerClass.getClass().getSimpleName();
	if (received.equalsIgnoreCase("AnalyticsTracker_Proxy"))
		validate = true;
	assertTrue(validate);
	}

}

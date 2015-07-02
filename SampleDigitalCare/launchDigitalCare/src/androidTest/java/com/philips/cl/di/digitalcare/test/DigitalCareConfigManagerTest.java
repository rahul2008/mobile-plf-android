package com.philips.cl.di.digitalcare.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class DigitalCareConfigManagerTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public DigitalCareConfigManagerTest() {
		super(LaunchDigitalCare.class);
	}

	private DigitalCareConfigManager mConfigManager = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mConfigManager = mock(DigitalCareConfigManager.class);
	}

	@SmallTest
	public void testIsConfigurationManagerIsMocked() {
		boolean validate = false;
		String received = mConfigManager.getClass().getSimpleName();
		if (received.equalsIgnoreCase("DigitalCareConfigManager_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

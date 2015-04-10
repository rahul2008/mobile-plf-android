package com.philips.cl.di.digitalcare.facebook.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.social.facebook.FacebookUtility;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class FacebookUtilityTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public FacebookUtilityTest() {
		super(LaunchDigitalCare.class);
	}

	private FacebookUtility mFacebookUtility = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mFacebookUtility = mock(FacebookUtility.class);
	}

	@SmallTest
	public void testIsFaceBookUtilityIsMocked() {
		boolean validate = false;
		String received = mFacebookUtility.getClass().getSimpleName();
		if (received.equalsIgnoreCase("FacebookUtility_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

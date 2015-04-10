package com.philips.cl.di.digitalcare.twitter.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthenticationActivity;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class TwitterAuthenticationActivityTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public TwitterAuthenticationActivityTest() {
		super(LaunchDigitalCare.class);
	}

	private TwitterAuthenticationActivity mTwitterAuthObject = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mTwitterAuthObject = mock(TwitterAuthenticationActivity.class);
	}

	@SmallTest
	public void testTwitterAuthenActivityIsMocked() {
		boolean validate = false;
		String received = mTwitterAuthObject.getClass().getSimpleName();
		if (received.equalsIgnoreCase("TwitterAuthenticationActivity_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

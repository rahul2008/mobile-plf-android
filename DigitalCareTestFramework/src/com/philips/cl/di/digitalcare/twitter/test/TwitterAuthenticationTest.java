package com.philips.cl.di.digitalcare.twitter.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class TwitterAuthenticationTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public TwitterAuthenticationTest() {
		super(LaunchDigitalCare.class);
	}

	private TwitterAuthentication mTwitterAuthentication = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mTwitterAuthentication = mock(TwitterAuthentication.class);
	}

	@SmallTest
	public void testIsTwitterAuthenticationisClassIsMocked() {
		boolean validate = false;
		String received = mTwitterAuthentication.getClass().getSimpleName();
		if (received.equalsIgnoreCase("TwitterAuthentication_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

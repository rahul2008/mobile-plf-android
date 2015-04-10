package com.philips.cl.di.digitalcare.twitter.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.social.twitter.TwitterPost;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public final class TwitterPostCallback extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public TwitterPostCallback() {
		super(LaunchDigitalCare.class);
	}

	private TwitterPost mTwitterPost = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mTwitterPost = mock(TwitterPost.class);
	}

	@SmallTest
	public void testIsTwitterPostIsMocked() {
		boolean validate = false;
		String received = mTwitterPost.getClass().getSimpleName();
		if (received.equalsIgnoreCase("TwitterPost_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

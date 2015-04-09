package com.philips.cl.di.digitalcare.twitter.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthenticationActivity;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthenticationCallback;
import com.philips.cl.di.digitalcare.social.twitter.TwitterPost;
import com.philips.cl.di.digitalcare.social.twitter.TwitterSupportFragment;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class TwitterScreenTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public TwitterScreenTest() {
		super(LaunchDigitalCare.class);
	}

	private TwitterSupportFragment mApplication = null;
	private TwitterAuthentication mTwitterAuth = null;
	private TwitterAuthenticationActivity mTwitterAuthActivity = null;
	private TwitterAuthenticationCallback mTwitterAuthCallback = null;
	private TwitterPost mTwitterPost = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mApplication = mock(TwitterSupportFragment.class);
		mTwitterAuth = mock(TwitterAuthentication.class);
		mTwitterAuthActivity = mock(TwitterAuthenticationActivity.class);
		mTwitterAuthCallback = mock(TwitterAuthenticationCallback.class);
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

	@SmallTest
	public void testIsTwitterAuthCallbackIsMocked() {
		boolean validate = false;
		String received = mTwitterAuthCallback.getClass().getSimpleName();
		if (received.equalsIgnoreCase("$Proxy0"))
			validate = true;
		assertTrue(validate);
	}

	@SmallTest
	public void testIsTwitterAuthenActivityIsMocked() {
		boolean validate = false;
		String received = mTwitterAuthActivity.getClass().getSimpleName();
		if (received.equalsIgnoreCase("TwitterAuthenticationActivity_Proxy"))
			validate = true;
		assertTrue(validate);
	}

	@SmallTest
	public void testIsTwitterSDKAuthenIsMocked() {
		boolean validate = false;
		String received = mTwitterAuth.getClass().getSimpleName();
		if (received.equalsIgnoreCase("TwitterAuthentication_Proxy"))
			validate = true;
		assertTrue(validate);
	}

	@SmallTest
	public void testIsTwitterScreenFragmentIsMocked() {
		boolean validate = false;
		String received = mApplication.getClass().getSimpleName();
		if (received.equalsIgnoreCase("TwitterSupportFragment_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

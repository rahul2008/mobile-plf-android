package com.philips.cl.di.digitalcare.facebook.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.philips.cl.di.digitalcare.social.facebook.FacebookAuthenticate;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class FacebookAuthenticateTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public FacebookAuthenticateTest() {
		super(LaunchDigitalCare.class);
	}

	private FacebookAuthenticate mFaceBookAuthCallback = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mFaceBookAuthCallback = mock(FacebookAuthenticate.class);
	}

	/*@SmallTest
	public void testFacebookAuthenticateObjectIsMocked() {
		boolean validate = false;
		Log.d("Naveen", "FaceBookAuthentication : "+ mFaceBookAuthCallback.getClass().getSimpleName());
		String received = mFaceBookAuthCallback.getClass().getSimpleName();
		if (received.equalsIgnoreCase("$Proxy0"))
			validate = true;
		assertTrue(validate);
	}*/

}

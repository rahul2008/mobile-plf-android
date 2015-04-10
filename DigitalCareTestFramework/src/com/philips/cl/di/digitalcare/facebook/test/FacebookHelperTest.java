package com.philips.cl.di.digitalcare.facebook.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.social.facebook.FacebookHelper;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

public class FacebookHelperTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public FacebookHelperTest() {
		super(LaunchDigitalCare.class);
	}

	private FacebookHelper mFaceBookHelperObject = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mFaceBookHelperObject = mock(FacebookHelper.class);
	}

	@SmallTest
	public void testIsFacebookHelperMocked() {
		boolean validate = false;
		String received = mFaceBookHelperObject.getClass().getSimpleName();
		if (received.equalsIgnoreCase("FacebookHelper_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

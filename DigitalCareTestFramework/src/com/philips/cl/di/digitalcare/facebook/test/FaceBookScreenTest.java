package com.philips.cl.di.digitalcare.facebook.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.DigitalCareActivity;
import com.philips.cl.di.digitalcare.social.facebook.FacebookHelper;
import com.philips.cl.di.digitalcare.social.facebook.FacebookScreenFragment;
import com.philips.cl.di.digitalcare.social.facebook.FacebookUtility;

public class FaceBookScreenTest extends
		ActivityInstrumentationTestCase2<DigitalCareActivity> {

	public FaceBookScreenTest() {
		super(DigitalCareActivity.class);
	}

	private FacebookScreenFragment mFacebookScreen = null;
	private FacebookHelper mFacebookHelper = null;
	private FacebookUtility mFacebookUtility = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mFacebookScreen = mock(FacebookScreenFragment.class);
		mFacebookHelper = mock(FacebookHelper.class);
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
	
	@SmallTest
	public void testIsFaceBookHelperIsMocked() {
		boolean validate = false;
		String received = mFacebookHelper.getClass().getSimpleName();
		if (received.equalsIgnoreCase("FacebookHelper_Proxy"))
			validate = true;
		assertTrue(validate);
	}
	
	@SmallTest
	public void testIsFaceBookScreenFragmentIsMocked() {
		boolean validate = false;
		String received = mFacebookScreen.getClass().getSimpleName();
		if (received.equalsIgnoreCase("FacebookScreenFragment_Proxy"))
			validate = true;
		assertTrue(validate);
	}

}

package com.philips.cl.di.dev.digitalcare.test;

import static org.mockito.Mockito.mock;


import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.dev.digitalcare.DigitalCareActivity;
import com.philips.cl.di.dev.digitalcare.DigitalCareApplication;
import com.philips.cl.di.dev.digitalcare.R;
import com.philips.cl.di.dev.digitalcare.Utility.test.Log;

/**
 * 
 * @author naveen@philips.com
 * @description Testing the DigitalCareApplication codesnippet testing.
 * @Since Mar 10, 2015
 */
public class DigitalCareApplicationTest extends
		ActivityInstrumentationTestCase2<DigitalCareActivity> {

	public DigitalCareApplicationTest() {
		super(DigitalCareActivity.class);
	}

	private Context mContext = null;
	private String TAG = DigitalCareApplicationTest.class.getSimpleName();
	private DigitalCareApplication mApplication = null;
	public static int[] mTestValues = { 2, 5, 6 };

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp launched");
		System.setProperty( "dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		mContext = getInstrumentation().getTargetContext();
		mApplication = mock(DigitalCareApplication.class);
		// when(DigitalCareApplication.getFeatureListKeys()).thenReturn(mTestValues);
	}

	@SmallTest
	public void testContextOftheDigitalCareApplication() {
		Log.d(TAG, "testContext");
		assertNotNull(mContext);
	}

	@SmallTest
	public void testIsDigitalCareApplicationMocked() {
		boolean validate = false;
		String received = mApplication.getClass().getSimpleName();
		if (received.equalsIgnoreCase("DigitalCareApplication_Proxy"))
			validate = true;
		assertTrue(validate);
	}

	@SmallTest
	public void testDigitalCareApplicationOptionsSupporting() {
		int[] mOptions = null;
		boolean mAvailable = false;
		mOptions = mContext.getResources().getIntArray(
				R.array.options_available);
		mAvailable = (mOptions.length > 0) ? true : false;
		assertTrue(mAvailable);
	}

	@SmallTest
	public void testFeaturesAvailable() {
		int[] mOptions = null;
		boolean mAvailable = false;
		mOptions = mContext.getResources().getIntArray(
				R.array.options_available);
		mAvailable = (mOptions.length == 6) ? true : false;
		assertTrue(mAvailable);
	}

	@SmallTest
	public void testgetFeatureListLogic() {

		int expected = 6;
		DigitalCareApplication mApplication = new DigitalCareApplication(
				mContext);

		Log.d(TAG, "Spy value : " + DigitalCareApplication.getFeatureListKeys());

		assertEquals(expected,
				DigitalCareApplication.getFeatureListKeys().length);
	}
}

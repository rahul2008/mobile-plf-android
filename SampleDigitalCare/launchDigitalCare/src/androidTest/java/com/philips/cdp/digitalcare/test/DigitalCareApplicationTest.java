package com.philips.cdp.digitalcare.test;

import static org.mockito.Mockito.mock;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cdp.sampledigitalcareapp.LaunchDigitalCare;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.Utility.test.Log;

/**
 * 
 * @author naveen@philips.com
 * @description Testing the DigitalCareConfigManager codesnippet testing.
 * @Since Mar 10, 2015
 */
public class DigitalCareApplicationTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	public DigitalCareApplicationTest() {
		super(LaunchDigitalCare.class);
	}

	private Context mContext = null;
	private String TAG = DigitalCareApplicationTest.class.getSimpleName();
	private DigitalCareConfigManager mApplication = null;
	public static int[] mTestValues = { 2, 5, 6 };

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp launched");
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mContext = getInstrumentation().getTargetContext();
		mApplication = mock(DigitalCareConfigManager.class);
		// when(DigitalCareConfigManager.getFeatureListKeys()).thenReturn(mTestValues);
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
		if (received.equalsIgnoreCase("DigitalCareConfigManager_Proxy"))
			validate = true;
		assertTrue(validate);
	}

	@SmallTest
	public void testDigitalCareApplicationOptionsSupporting() {
		int[] mOptions = null;
		boolean mAvailable = false;
		mOptions = mContext.getResources().getIntArray(
				R.array.main_menu_title);
		mAvailable = (mOptions.length > 0) ? true : false;
		assertTrue(mAvailable);
	}

	/*@SmallTest
	public void testFeaturesAvailable() {
		int[] mOptions = null;
		boolean mAvailable = false;
		mOptions = mContext.getResources().getIntArray(
				R.array.main_menu_title);
		mAvailable = (mOptions.length == 6) ? true : false;
		assertTrue(mAvailable);
	}*/
//
//	@SmallTest
//	public void testgetFeatureListLogic() {
//
//		int expected = 6;
//		DigitalCareConfigManager mApplication = new DigitalCareConfigManager(
//				mContext);
//
//		Log.d(TAG, "Spy value : " + DigitalCareConfigManager.getFeatureListKeys());
//
//		assertEquals(expected,
//				DigitalCareConfigManager.getFeatureListKeys().length);
//	}
}

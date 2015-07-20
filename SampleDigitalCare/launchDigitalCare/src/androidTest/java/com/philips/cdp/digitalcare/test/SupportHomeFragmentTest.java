package com.philips.cdp.digitalcare.test;

import static org.mockito.Mockito.mock;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

import com.philips.cdp.digitalcare.SupportHomeFragment;
import com.philips.cdp.digitalcare.Utility.test.Log;
import com.philips.cdp.sampledigitalcareapp.LaunchDigitalCare;

public class SupportHomeFragmentTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	private final String TAG = SupportHomeFragmentTest.class.getSimpleName();
	private SupportHomeFragment mHomeFragment = null;

	public SupportHomeFragmentTest() {
		super(LaunchDigitalCare.class);
	}

	private SupportHomeFragment mSupportHomeFragment = null;
	private Context mContext = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mContext = getInstrumentation().getTargetContext();
		mSupportHomeFragment = mock(SupportHomeFragment.class);
		mHomeFragment = new SupportHomeFragment();
	}

	@SmallTest
	public void testIsSupportHomeFragmentIsMocked() {
		boolean validate = false;
		String received = mSupportHomeFragment.getClass().getSimpleName();
		if (received.equalsIgnoreCase("SupportHomeFragment_Proxy"))
			validate = true;
		assertTrue(validate);
	}

	/*@SmallTest
	public void testFragmentViewContainer() {

		RelativeLayout mContainer = new RelativeLayout(mContext);
		mContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));

		View mReceivedView = mHomeFragment.onCreateView(getActivity()
				.getLayoutInflater(), mContainer, null);
		DLog.d(TAG, "Received " + mReceivedView);
		assertNotNull(mReceivedView);
	}*/

	@SmallTest
	public void testFragmentCreation() {
		Bundle mBundle = new Bundle();
		mSupportHomeFragment.onActivityCreated(mBundle);
		Configuration config = mContext.getResources().getConfiguration();
		mSupportHomeFragment.onConfigurationChanged(config);
		mSupportHomeFragment.setViewParams(config);
		mSupportHomeFragment.onClick(new View(mContext));
		assertNotNull(mSupportHomeFragment);
	}

	@SmallTest
	public void testActionBarTitle() {
		String mReceived = mSupportHomeFragment.getActionbarTitle();
		Log.d(TAG, "ActionaBAr Title is : "+ mReceived);
		assertNull(mReceived);
	}

}

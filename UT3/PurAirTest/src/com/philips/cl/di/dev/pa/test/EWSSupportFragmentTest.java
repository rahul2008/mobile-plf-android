package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSSupportFragment;

public class EWSSupportFragmentTest extends
		ActivityInstrumentationTestCase2<EWSActivity> {
	private EWSActivity activity;

	public EWSSupportFragmentTest() {
		super(EWSActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}

	public void testFragmentTag() {
		EWSSupportFragment ewsSupportFragment = new EWSSupportFragment();
		activity.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.setup_fragment_container, ewsSupportFragment, EWSConstant.EWS_SUPPORT_FRAGMENT_TAG)
				.commitAllowingStateLoss();

		assertEquals(EWSConstant.EWS_SUPPORT_FRAGMENT_TAG,
				ewsSupportFragment.getTag());
	}
}
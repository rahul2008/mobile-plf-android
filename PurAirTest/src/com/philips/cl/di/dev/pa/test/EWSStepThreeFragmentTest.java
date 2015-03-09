package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSStepFourFragment;

public class EWSStepThreeFragmentTest extends
		ActivityInstrumentationTestCase2<EWSActivity> {
	private EWSActivity activity;

	public EWSStepThreeFragmentTest() {
		super(EWSActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}

	public void testFragmentTag() {
		EWSStepFourFragment ewsStepThreeFragment = new EWSStepFourFragment();
		activity.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.setup_fragment_container, ewsStepThreeFragment, EWSConstant.EWS_STEP_THREE_FRAGMENT_TAG)
				.commitAllowingStateLoss();

		assertEquals(EWSConstant.EWS_STEP_THREE_FRAGMENT_TAG,
				ewsStepThreeFragment.getTag());
	}
}
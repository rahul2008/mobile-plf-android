package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSStepTwoFragment;

public class EWSStepTwoFragmentTest extends
		ActivityInstrumentationTestCase2<EWSActivity> {
	private EWSActivity activity;

	public EWSStepTwoFragmentTest() {
		super(EWSActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}

	public void testFragmentTag() {
		EWSStepTwoFragment ewsStepTwoFragment = new EWSStepTwoFragment();
		activity.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.setup_fragment_container, ewsStepTwoFragment, EWSConstant.EWS_STEP_TWO_FRAGMENT_TAG)
				.commitAllowingStateLoss();

		assertEquals(EWSConstant.EWS_STEP_TWO_FRAGMENT_TAG,	ewsStepTwoFragment.getTag());
	}
}
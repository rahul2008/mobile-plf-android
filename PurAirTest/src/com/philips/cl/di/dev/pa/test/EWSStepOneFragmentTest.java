package com.philips.cl.di.dev.pa.test;

import android.annotation.SuppressLint;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSStepOneFragment;

public class EWSStepOneFragmentTest extends
	ActivityInstrumentationTestCase2<EWSActivity> {
	private EWSActivity activity;

	public EWSStepOneFragmentTest() {
		super(EWSActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}
	
	public void testFragmentTag() {
		EWSStepOneFragment ewsStepOneFragment = new EWSStepOneFragment();
		activity.getSupportFragmentManager().beginTransaction()
			.add(R.id.ews_fragment_container, ewsStepOneFragment, EWSConstant.EWS_STEP_ONE_FRAGMENT_TAG)
			.commit();
		
		assertEquals(EWSConstant.EWS_STEP_ONE_FRAGMENT_TAG, ewsStepOneFragment.getTag());
	}
}
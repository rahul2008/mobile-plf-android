package com.philips.cl.di.dev.pa.activity;

import android.os.Bundle;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class TermsAndConditionsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.terms_and_conditions);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.TERMS_AND_CONDITIONS);
	}
}

package com.philips.cl.di.dev.pa.digitalcare;

import android.content.Intent;
import android.os.Bundle;

import com.philips.cl.di.dev.pa.digitalcare.fragment.FacebookScreenFragment;
import com.philips.cl.di.dev.pa.digitalcare.fragment.SupportHomeFragment;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;

/*
 *	DigitalCareActivity  is the main container class for Digital Care fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2015
 */
public class DigitalCareActivity extends DigitalCareBaseActivity {
	private static final String TAG = "DigitalCareActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DigitalCareApplication.getDigitalCareInstance(this);
		setContentView(R.layout.activity_digi_care);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(TAG, "Actionbar: " + e.getMessage());
		}
		showFragment(new SupportHomeFragment());
		enableActionBarHome();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ALog.i(TAG, "DigitalCareActivity onActivityResult");
		FacebookScreenFragment fbFrag = new FacebookScreenFragment();
		fbFrag.onActivityResultFragment(this, requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}

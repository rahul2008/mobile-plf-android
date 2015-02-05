package com.philips.cl.di.dev.pa.digitalcare;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.philips.cl.di.dev.pa.digitalcare.fragment.ContactUsFragment;
import com.philips.cl.di.dev.pa.digitalcare.fragment.SupportHomeFragment;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;

/*
 *	DigitalCareActivity  is the main container class for Digital Care fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2014
 */
public class DigitalCareActivity extends BaseActivity {
	private static final String TAG = "DigitalCareActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		new Session.OpenRequest(this);
		
		ContactUsFragment contactUsFrag = new ContactUsFragment();
		contactUsFrag.onActivityResultFragment(this, requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}

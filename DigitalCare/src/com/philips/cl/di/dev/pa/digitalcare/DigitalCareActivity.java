package com.philips.cl.di.dev.pa.digitalcare;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

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
	}

	@Override
	protected void onResume() {
		super.onResume();
//		showFragment(new SupportHomeFragment());
	}

	public void showFragment() {
		Fragment fragment = new SupportHomeFragment();
		try {
//			getSupportFragmentManager().popBackStackImmediate(null,
//					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(R.id.mainContainer, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(TAG, e.getMessage());
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);
		}
	}
}


package com.philips.cl.di.reg.ui.traditional;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.listener.RegistrationTitleBarListener;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegistrationActivity extends FragmentActivity implements OnClickListener,
        RegistrationTitleBarListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity  Register: NetworStateListener");
		initUI();
	}

	@Override
	protected void onStart() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onDestroy");
		RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity Unregister: NetworStateListener,Context");
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onBackPressed");
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager
		        .findFragmentByTag(RegConstants.REGISTRATION_FRAGMENT_TAG);
		if (fragment != null) {
			if (((RegistrationFragment) fragment).onBackPressed()) {
				// not consumed vertical code goes here // actual code
				super.onBackPressed();
			} else {
				// consumed
			}
		}
	}

	private void initUI() {
		ImageView ivBack = (ImageView) findViewById(R.id.iv_reg_back);
		ivBack.setOnClickListener(this);
		launchRegistrationFragment(R.id.fl_reg_fragment_container, this);
	}

	/**
	 * Launch registration fragment
	 */
	private void launchRegistrationFragment(int container, FragmentActivity fragmentActivity) {
		try {
			FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
			RegistrationFragment registrationFragment = new RegistrationFragment();
			registrationFragment.setOnUpdateTitleListener(this);
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(container, registrationFragment,
			        RegConstants.REGISTRATION_FRAGMENT_TAG);
			fragmentTransaction.commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			RLog.e(RLog.EXCEPTION,
			        "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
			                + e.getMessage());
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_reg_back) {
			onBackPressed();
		}
	}

	@Override
	public void updateRegistrationTitle(int titleResourceID) {
		// Update title only and show hamberger
		TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
		tvTitle.setText(getString(titleResourceID));
	}

	@Override
	public void updateRegistrationTitleWithBack(int titleResourceID) {
		// update title only and show back
		TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
		tvTitle.setText(getString(titleResourceID));
	}

}

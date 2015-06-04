
package com.philips.cl.di.reg.ui.traditional;

import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.events.NetworStateListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.settings.RegistrationHelper.Janrain;
import com.philips.cl.di.reg.ui.social.AlmostDoneFragment;
import com.philips.cl.di.reg.ui.social.MergeAccountFragment;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegistrationActivity extends FragmentActivity implements NetworStateListener,
        OnClickListener {

	private FragmentManager mFragmentManager = null;

	private final String TAG = TextView.class.getSimpleName();

	private static final boolean VERIFICATION_SUCCESS = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		mFragmentManager = getSupportFragmentManager();
		initUI();
		loadFirstFragment();
	}

	@Override
	public void onBackPressed() {
		hideKeyBoard();
		handleBackStack();
	}

	/*
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		RegistrationHelper.getInstance().unregisterListener(getApplicationContext());
		super.onDestroy();
	}

	private void handleBackStack() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		int count = fragmentManager.getBackStackEntryCount();
		if (count == 0) {
			super.onBackPressed();
			return;
		}
		Fragment fragment = fragmentManager.getFragments().get(count);
		if (fragment instanceof WelcomeFragment) {
			navigateToHome();
		} else {
			super.onBackPressed();
		}
	}

	public void loadFirstFragment() {
		try {
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fl_reg_fragment_container, new HomeFragment());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			RLog.e(TAG, "FragmentTransaction Exception occured :" + e);
		}
	}

	public void addFragment(Fragment fragment) {
		try {
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction
			        .replace(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			RLog.e(TAG, "FragmentTransaction Exception occured :" + e);
		}
		hideKeyBoard();
	}

	public void navigateToHome() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		int fragmentCount = fragmentManager.getBackStackEntryCount();
		for (int i = fragmentCount; i >= 1; i--) {
			fragmentManager.popBackStack();
		}
	}

	private void initUI() {
		ImageView ivBack = (ImageView) findViewById(R.id.iv_reg_back);
		ivBack.setOnClickListener(this);
	}

	public void addWelcomeFragmentOnVerification() {
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		Bundle welcomeFragmentBundle = new Bundle();
		welcomeFragmentBundle.putBoolean(RegConstants.VERIFICATIN_SUCCESS, VERIFICATION_SUCCESS);
		welcomeFragment.setArguments(welcomeFragmentBundle);
		addFragment(welcomeFragment);
	}

	public void addAlmostDoneFragment(JSONObject preFilledRecord, String provider,
	        String registrationToken) {
		AlmostDoneFragment socialAlmostDoneFragment = new AlmostDoneFragment();
		Bundle socialAlmostDoneFragmentBundle = new Bundle();
		socialAlmostDoneFragmentBundle.putString(RegConstants.SOCIAL_TWO_STEP_ERROR,
		        preFilledRecord.toString());
		socialAlmostDoneFragmentBundle.putString(RegConstants.SOCIAL_PROVIDER, provider);
		socialAlmostDoneFragmentBundle.putString(RegConstants.SOCIAL_REGISTRATION_TOKEN,
		        registrationToken);
		socialAlmostDoneFragment.setArguments(socialAlmostDoneFragmentBundle);
		addFragment(socialAlmostDoneFragment);
	}

	public void addMergeAccountFragment(String registrationToken, String provider) {
		MergeAccountFragment mergeAccountFragment = new MergeAccountFragment();
		Bundle mergeFragmentBundle = new Bundle();
		mergeFragmentBundle.putString(RegConstants.SOCIAL_PROVIDER, provider);
		mergeFragmentBundle.putString(RegConstants.SOCIAL_MERGE_TOKEN, registrationToken);
		mergeAccountFragment.setArguments(mergeFragmentBundle);
		addFragment(mergeAccountFragment);
	}

	/*
	 * @Override
	 * public void onEventReceived(String event) {
	 * if (RegConstants.IS_ONLINE.equals(event)) {
	 * if (!RegistrationHelper.getInstance().isJanrainIntialized()) {
	 * RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
	 * registrationSettings.intializeRegistrationSettings(Janrain.REINITIALIZE, this,
	 * Locale.getDefault());
	 * }
	 * }
	 * }
	 */

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}
	}

	public void handleContinue() {
		this.finish();
		// TODO: need to notify app onSuccessful login
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_reg_back) {
			onBackPressed();
		}
	}

	@Override
	public void onNetWorkStateReceived(boolean isOnline) {
		if (!RegistrationHelper.getInstance().isJanrainIntialized()) {
			RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
			registrationSettings.intializeRegistrationSettings(Janrain.REINITIALIZE, this,
			        Locale.getDefault());
		}

	}

}

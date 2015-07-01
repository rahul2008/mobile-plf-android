
package com.philips.cl.di.reg.ui.traditional;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.adobe.mobile.Config;
import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsPages;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsUtils;
import com.philips.cl.di.reg.events.NetworStateListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.settings.RegistrationHelper.Janrain;
import com.philips.cl.di.reg.ui.social.AlmostDoneFragment;
import com.philips.cl.di.reg.ui.social.MergeAccountFragment;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

import org.json.JSONObject;

import java.util.Locale;

public class RegistrationActivity extends FragmentActivity implements NetworStateListener,
        OnClickListener {

	private FragmentManager mFragmentManager;

	private final boolean VERIFICATION_SUCCESS = true;

	private Handler mSiteCatalistHandler = new Handler();

	private Runnable mPauseSiteCatalystRunnable = new Runnable() {

		@Override
		public void run() {
			Config.pauseCollectingLifecycleData();
		}
	};

	private Runnable mResumeSiteCatalystRunnable = new Runnable() {

		@Override
		public void run() {
			Config.collectLifecycleData();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onCreate");
		setContentView(R.layout.activity_registration);
		RegistrationHelper.getInstance().registerNetworkStateListener(this);
		RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity  Register: NetworStateListener");
		mFragmentManager = getSupportFragmentManager();
		initUI();
		loadFirstFragment();
	}

	@Override
	protected void onStart() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onResume");
		mSiteCatalistHandler.removeCallbacksAndMessages(null);
		mSiteCatalistHandler.post(mResumeSiteCatalystRunnable);
		super.onResume();
	}

	@Override
	protected void onPause() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onPause");
		mSiteCatalistHandler.removeCallbacksAndMessages(null);
		mSiteCatalistHandler.post(mPauseSiteCatalystRunnable);
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
		RegistrationHelper.getInstance().unregisterListener(getApplicationContext());
		RegistrationHelper.getInstance().unRegisterNetworkListener(this);
		RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity Unregister: NetworStateListener,Context");
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onBackPressed");
		hideKeyBoard();
		handleBackStack();
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
			handleUserLoginStateFragments();

		} catch (IllegalStateException e) {
			RLog.e(RLog.EXCEPTION,
			        "RegistrationActivity :FragmentTransaction Exception occured in loadFirstFragment  :"
			                + e.getMessage());
		}
	}

	private void handleUserLoginStateFragments() {
		User mUser = new User(this.getApplicationContext());
		if (mUser.getEmailVerificationStatus(this.getApplicationContext())) {
			replaceWithWelcomeFragment();
			return;
		}
		replaceWithHomeFragment();
	}

	private void trackPage(String prevPage, String currPage) {
		AnalyticsUtils.trackPage(prevPage, currPage);
	}

	public void replaceWithHomeFragment() {
		try {
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fl_reg_fragment_container, new HomeFragment());
			fragmentTransaction.commitAllowingStateLoss();
			trackPage("", AnalyticsPages.HOME);
		} catch (IllegalStateException e) {
			RLog.e(RLog.EXCEPTION,
			        "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
			                + e.getMessage());
		}
	}

	public void addFragment(Fragment fragment) {
		try {
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.add(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			RLog.e(RLog.EXCEPTION,
			        "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
			                + e.getMessage());
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

	private void replaceWithWelcomeFragment() {
		try {
			WelcomeFragment welcomeFragment = new WelcomeFragment();
			Bundle welcomeFragmentBundle = new Bundle();
			welcomeFragmentBundle
			        .putBoolean(RegConstants.VERIFICATIN_SUCCESS, VERIFICATION_SUCCESS);
			welcomeFragmentBundle.putBoolean(RegConstants.IS_FROM_BEGINING, true);
			welcomeFragment.setArguments(welcomeFragmentBundle);
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fl_reg_fragment_container, welcomeFragment);
			fragmentTransaction.commitAllowingStateLoss();
			trackPage("", AnalyticsPages.WELCOME);
		} catch (IllegalStateException e) {
			RLog.e(RLog.EXCEPTION,
			        "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
			                + e.getMessage());
		}
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

	public void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}
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
			RLog.d(RLog.NETWORK_STATE, "RegistrationActivity :onNetWorkStateReceived");
			RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
			registrationSettings.intializeRegistrationSettings(Janrain.REINITIALIZE, this,
			        Locale.getDefault());
			RLog.d(RLog.JANRAIN_INITIALIZE,
			        "RegistrationActivity : Janrain reinitialization with locale : "
			                + Locale.getDefault());
		}
	}
}

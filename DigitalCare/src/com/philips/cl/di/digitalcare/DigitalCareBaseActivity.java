package com.philips.cl.di.digitalcare;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * DigitalCareBaseActivity is the main super abstract class container for
 * DigitalCare Activity.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 */
public abstract class DigitalCareBaseActivity extends Activity {
	private ImageView mActionBarMenuIcon = null;;
	private ImageView mActionBarArrow = null;
	private DigitalCareFontTextView mActionBarTitle = null;
	private FragmentManager fragmentManager = null;
	private DigitalCareConfigManager mDigitalCareConfigManager = null;
	private static String TAG = "DigitalCareBaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		DLog.i(TAG, "onCreate");
		setLocaleLanguage();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(DigitalCareConfigManager.getAnimationStart(),
				DigitalCareConfigManager.getAnimationStop());
		DigitalCareConfigManager.getInstance(this);
		fragmentManager = getFragmentManager();
	}

	protected void initActionBar() throws ClassCastException {
		mActionBarMenuIcon = (ImageView) findViewById(R.id.home_icon);
		mActionBarArrow = (ImageView) findViewById(R.id.back_to_home_img);
		mActionBarTitle = (DigitalCareFontTextView) findViewById(R.id.action_bar_title);

		mActionBarMenuIcon.setOnClickListener(actionBarClickListener);
		mActionBarArrow.setOnClickListener(actionBarClickListener);
		// enableActionBarHome();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DLog.i(TAG, TAG + " : onConfigurationChanged ");
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsTracker.startCollectLifecycleData();
		AnalyticsTracker.trackAction(
				AnalyticsConstants.ACTION_KEY_SET_APP_STATUS,
				AnalyticsConstants.ACTION_KEY_APP_STATUS,
				AnalyticsConstants.ACTION_VALUE_FOREGROUND);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AnalyticsTracker.trackAction(
				AnalyticsConstants.ACTION_KEY_SET_APP_STATUS,
				AnalyticsConstants.ACTION_KEY_APP_STATUS,
				AnalyticsConstants.ACTION_VALUE_BACKGROUND);
		AnalyticsTracker.stopCollectLifecycleData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return backstackFragment();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (mDigitalCareConfigManager != null) {
			mDigitalCareConfigManager = null;
		}
	}

	private void setLocaleLanguage() {

		Locale locale = new Locale(DigitalCareConfigManager.getLanguage(),
				DigitalCareConfigManager.getCountry());
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

//		Toast.makeText(
//				getApplicationContext(),
//				"Language set to " + DigitalCareConfigManager.getLanguage()
//						+ "\n Country set to"
//						+ DigitalCareConfigManager.getCountry(),
//				Toast.LENGTH_SHORT).show();
	}

	private boolean backstackFragment() {

		if (fragmentManager.getBackStackEntryCount() == 1) {
			this.finish();
			// overridePendingTransition(R.anim.left_in, R.anim.right_out);
		}

		else if (fragmentManager.getBackStackEntryCount() == 2) {
			enableActionBarHome();
			fragmentManager.popBackStack();
			removeCurrentFragment();
		} else {
			fragmentManager.popBackStack();
			removeCurrentFragment();
		}
		return false;
	}

	private void removeCurrentFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		Fragment currentFrag = fragmentManager
				.findFragmentById(R.id.mainContainer);

		if (currentFrag != null) {
			transaction.remove(currentFrag);
		}

		transaction.commit();
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			int _id = view.getId();
			if (_id == R.id.home_icon) {
				finish();
				// overridePendingTransition(R.anim.left_in, R.anim.right_out);
			} else if (_id == R.id.back_to_home_img)
				backstackFragment();
		}
	};

	private void enableActionBarLeftArrow() {
		mActionBarMenuIcon.setVisibility(View.GONE);
		mActionBarArrow.setVisibility(View.VISIBLE);
		mActionBarArrow.bringToFront();
	}

	protected void enableActionBarHome() {
		mActionBarMenuIcon.setVisibility(View.VISIBLE);
		mActionBarMenuIcon.bringToFront();
		mActionBarArrow.setVisibility(View.GONE);
		mActionBarTitle.setText(getResources().getString(
				R.string.actionbar_title_support));
	}

	protected void showFragment(Fragment fragment) {
		try {
			enableActionBarLeftArrow();

			// getSupportFragmentManager().popBackStackImmediate(null,
			// FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.mainContainer, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			DLog.e(TAG, e.getMessage());
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);
		}
	}
}

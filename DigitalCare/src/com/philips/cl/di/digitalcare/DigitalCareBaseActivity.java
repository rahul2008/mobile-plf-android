package com.philips.cl.di.digitalcare;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.philips.cl.di.digitalcare.contactus.ContactUsFragment;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cl.di.digitalcare.locatephilips.LocatePhilipsFragment;
import com.philips.cl.di.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cl.di.digitalcare.productregistration.ProductRegistrationFragment;
import com.philips.cl.di.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;
import com.philips.cl.di.digitalcare.util.FragmentObserver;

/*
 *	DigitalCareBaseActivity is the main super class container for Digital Care fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2015
 */
public abstract class DigitalCareBaseActivity extends Activity implements Observer {
	private ImageView mActionBarMenuIcon = null;;
	private ImageView mActionBarArrow = null;
	private DigitalCareFontTextView mActionBarTitle = null;

	private static String TAG = "DigitalCareBaseActivity";
	private FragmentObserver mFragmentObserver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		Log.i(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.slide_in_bottom,
				R.anim.slide_out_bottom);
	}

	private void setFragmentDetails(String actionbarTitle) {
		mActionBarTitle.setText(actionbarTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mFragmentObserver = DigitalCareApplication.getFragmentObserverInstance();
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
		Log.i(TAG, TAG + " : onConfigurationChanged ");
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

	private FragmentManager fragmentManager = getFragmentManager();

	private boolean backstackFragment() {

		if (fragmentManager.getBackStackEntryCount() == 1) {
			this.finish();
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
			} else if (_id == R.id.back_to_home_img)
				backstackFragment();
		}
	};

	private void optionSelected(int value) {
		switch (value) {
		case DigitalCareContants.OPTION_CONTACT_US:
			showFragment(new ContactUsFragment());
			break;
		case DigitalCareContants.OPTION_PRODUCS_DETAILS:
			showFragment(new ProductDetailsFragment());
			break;
		case DigitalCareContants.OPTION_FAQ:
			break;
		case DigitalCareContants.OPTION_FIND_PHILIPS_NEARBY:
			showFragment(new LocatePhilipsFragment());
			break;
		case DigitalCareContants.OPTION_WHAT_ARE_YOU_THINKING:
			showFragment(new RateThisAppFragment());
			break;
		case DigitalCareContants.OPTION_REGISTER_PRODUCT:
			showFragment(new ProductRegistrationFragment());
			break;
		case DigitalCareContants.OPTION_NOTHING:
			break;
		}
	}

	@Override
	public void update(Observable observable, Object title) {
		optionSelected(mFragmentObserver.getOptionSelected());
		setFragmentDetails(mFragmentObserver.getActionbarTitle());
	}

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
			fragmentTransaction.add(R.id.mainContainer, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			Log.e(TAG, e.getMessage());
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);
		}
	}
}

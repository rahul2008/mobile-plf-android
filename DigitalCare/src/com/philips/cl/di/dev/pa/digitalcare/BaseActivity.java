package com.philips.cl.di.dev.pa.digitalcare;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.philips.cl.di.dev.pa.digitalcare.customview.FontTextView;
import com.philips.cl.di.dev.pa.digitalcare.fragment.ProductRegistrationFragment;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;
import com.philips.cl.di.dev.pa.digitalcare.util.DigiCareContants;
import com.philips.cl.di.dev.pa.digitalcare.util.FragmentObserver;

/*
 *	BaseActivity is the main super class container for Digital Care fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2014
 */
public class BaseActivity extends ActionBarActivity implements Observer {
	private ImageView leftMenu;
	private ImageView backToHome;
	private FontTextView actionBarTitle;

	private static String TAG = "BaseActivity";
	private FragmentObserver mFragmentObserver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		Log.i(TAG, "onCreate");
	}

	public void setFragmentDetails(String actionbarTitle) {
		actionBarTitle.setText(actionbarTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mFragmentObserver = DigitalCareApplication.getAppContext()
				.getObserver();
	}

	protected void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d = getResources().getDrawable(R.drawable.ews_nav_bar_2x);
		actionBar.setBackgroundDrawable(d);
		View viewActionbar = getLayoutInflater().inflate(
				R.layout.home_action_bar,
				(ViewGroup) findViewById(R.id.action_bar_lyt));
		leftMenu = (ImageView) viewActionbar.findViewById(R.id.left_menu_img);
		backToHome = (ImageView) viewActionbar
				.findViewById(R.id.back_to_home_img);
		actionBarTitle = (FontTextView) viewActionbar
				.findViewById(R.id.action_bar_title);
		actionBarTitle.setTypeface(Typeface.DEFAULT);

		leftMenu.setOnClickListener(actionBarClickListener);
		backToHome.setOnClickListener(actionBarClickListener);

		actionBar.setCustomView(viewActionbar);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ALog.i(TAG, TAG + " : onConfigurationChanged ");
		RelativeLayout.LayoutParams params = (LayoutParams) actionBarTitle
				.getLayoutParams();
		params.leftMargin = (int) getResources()
				.getDimension(R.dimen.actionbar_title_text_margin_left);
		actionBarTitle.setLayoutParams(params);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			List<Fragment> fragmentList = getSupportFragmentManager()
					.getFragments();
			ALog.i("testing", " KEYCODE_BACK : fragmentList : " + fragmentList
					+ " -- fragmentList.size() : " + fragmentList.size());
			if (fragmentList != null) {
				int size = fragmentList.size();
				if (fragmentList.get(size - 1) != null) {
					ALog.i(TAG,
							" KEYCODE_BACK : "
									+ fragmentList.get(fragmentList.size() - 1));
					if (size == 1) {
						finish();
					}
				} else /* if(fragmentList.get(size - 1) == null) */{
					// ALog.i(TAG,
					// " KEYCODE_BACK : "
					// + fragmentList.get(fragmentList.size() - 2));
					finish();
				}
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.left_menu_img:
				finish();
				break;
			case R.id.back_to_home_img:
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void optionSelected(int value) {
		// Log.i(TAG,
		// "BaseActivity optionSelected : "
		// + mFragmentObserver.getOptionSelected());
		switch (value) {
		case DigiCareContants.OPTION_CONTACT_US:
			break;
		case DigiCareContants.OPTION_PRODUCS_DETAILS:
			break;
		case DigiCareContants.OPTION_FAQ:
			break;
		case DigiCareContants.OPTION_FIND_PHILIPS_NEARBY:
			break;
		case DigiCareContants.OPTION_WHAT_ARE_YOU_THINKING:
			break;
		case DigiCareContants.OPTION_REGISTER_PRODUCT:
			showFragment(new ProductRegistrationFragment());
			break;
		default:
		}
	}

	@Override
	public void update(Observable observable, Object title) {
		actionBarTitle.setText(mFragmentObserver.getActionbarTitle());
		optionSelected(mFragmentObserver.getOptionSelected());
	}

	protected void showFragment(Fragment fragment) {
		try {
			// getSupportFragmentManager().popBackStackImmediate(null,
			// FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

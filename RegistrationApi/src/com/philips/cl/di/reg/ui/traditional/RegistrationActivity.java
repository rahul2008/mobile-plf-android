
package com.philips.cl.di.reg.ui.traditional;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.settings.RegistrationHelper.Janrain;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegistrationActivity extends FragmentActivity implements EventListener {

	private RelativeLayout mActionBarArrow = null;

	private FragmentManager mFragmentManager = null;

	private TextView mActionBarTitle = null;

	private final String TAG = TextView.class.getSimpleName();
	
	private final int HOME_SCREEN =1;
	private final int CREATE_ACCOUNT_SCREEN =2;
	private final int PHILIPS_WELCOME_SCREEN =4;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NetworkUtility.getInstance().checkIsOnline(getApplicationContext());
		EventHelper.getInstance().registerEventNotification(RegConstants.IS_ONLINE, this);

		setContentView(R.layout.activity_registration);
		mFragmentManager = getSupportFragmentManager();
		initUI();

		loadMainFragment();
		getSupportFragmentManager().addOnBackStackChangedListener(getListener());
	}

	public void loadMainFragment() {
		addFragment(new HomeFragment());
	}

	public void addFragment(Fragment fragment) {
		try {
			if (mFragmentManager.getBackStackEntryCount() == 0) {
				hideBackArrow();
			} else {
				enableActionBarLeftArrow();
			}
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.main_activity_container, fragment, fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			RLog.e(TAG, "FragmentTransaction Exception occured :" + e);
		}
		hideKeyBoard();
	}

	private OnBackStackChangedListener getListener() {
		OnBackStackChangedListener result = new OnBackStackChangedListener() {

			public void onBackStackChanged() {
				FragmentManager manager = getSupportFragmentManager();
				if (manager != null) {
					int backStackEntryCount = manager.getBackStackEntryCount();
					if (backStackEntryCount == 0) {
						finish();
					} else if (backStackEntryCount == 1) {
						hideBackArrow();
					}
				}
			}
		};
		return result;
	}

	private boolean backstackFragment() {
		
		if (mFragmentManager.getBackStackEntryCount() == HOME_SCREEN) {
			this.finish();
		}

		else if (mFragmentManager.getBackStackEntryCount() == CREATE_ACCOUNT_SCREEN) {
			hideBackArrow();
			mFragmentManager.popBackStack();
			removeCurrentFragment();
		}else if (mFragmentManager.getBackStackEntryCount() == PHILIPS_WELCOME_SCREEN) {
			System.out.println("**************** 4 *************");
			clearBackStack();
		} else {
			mFragmentManager.popBackStack();
			removeCurrentFragment();
		}
		
		hideKeyBoard();
		return false;
	}
	
	public void clearBackStack() {
		FragmentManager childFragmentManager = getSupportFragmentManager();
		int fragmentCount = childFragmentManager.getBackStackEntryCount();
		for (int i = 1; i < fragmentCount; i++) {
			childFragmentManager.popBackStack();
		}
	}
	
	private void removeCurrentFragment() {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();

		Fragment currentFrag = mFragmentManager.findFragmentById(R.id.main_activity_container);

		if (currentFrag != null) {
			transaction.remove(currentFrag);
		}

		transaction.commit();
	}

	protected void hideBackArrow() {
		mActionBarArrow.setVisibility(View.INVISIBLE);
		mActionBarTitle.setText(getResources().getString(R.string.sign_in));
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			backstackFragment();
		}
	};

	private void initUI() {
		mActionBarArrow = (RelativeLayout) findViewById(R.id.iv_backArrow);
		mActionBarTitle = (TextView) findViewById(R.id.action_bar_title);
		mActionBarArrow.setOnClickListener(actionBarClickListener);

	}

	private void enableActionBarLeftArrow() {
		mActionBarArrow.setVisibility(View.VISIBLE);
		mActionBarArrow.bringToFront();
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			if (!RegistrationHelper.isJanrainIntialized()) {

				RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
				registrationSettings.intializeRegistrationSettings(Janrain.REINITIALIZE, this);

			}
		}
	}

	@Override
	public void raiseEvent(String event) {
		// Do nothing
	}
	
	private void hideKeyBoard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

}

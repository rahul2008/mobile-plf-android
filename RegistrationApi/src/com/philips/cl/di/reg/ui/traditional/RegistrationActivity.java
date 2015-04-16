package com.philips.cl.di.reg.ui.traditional;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;

public class RegistrationActivity extends FragmentActivity {

	private ImageView mActionBarArrow = null;
	private FragmentManager mFragmentManager = null;
	private TextView mActionBarTitle = null;
	private final String PROD_CLIENT_ID = "mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3";
	private final String MICROSITE_ID = "81376";
	private final String REGISTRATION_USE_PROD = "REGISTRATION_USE_PRODUCTION";
	private final String REGISTRATION_USE_EVAL = "REGISTRATION_USE_EVAL";
	private final String EVAL_CLIENT_Id = "6v3yzffu6uxq4k9ctcw4jtd498k8zmtz";
	private final String TAG = TextView.class.getSimpleName();

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		mFragmentManager = getSupportFragmentManager();
		initialize();
		intializeJanrain(true);
		loadMainFragment();
		getSupportFragmentManager()
				.addOnBackStackChangedListener(getListener());
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
			FragmentTransaction fragmentTransaction = mFragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.main_activity_container, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			RLog.e(TAG, "FragmentTransaction Exception occured :" + e);
		}

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

	private final BroadcastReceiver janrainStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				Bundle extras = intent.getExtras();
				RLog.i(RLog.APPLICATION, "janrainStatusReceiver, intent = "
						+ intent.toString());
				if ((Jump.JR_DOWNLOAD_FLOW_SUCCESS.equalsIgnoreCase(intent
						.getAction())) && (null != extras)) {
				} else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW
						.equalsIgnoreCase(intent.getAction())
						&& (extras != null)) {
				}
			}
		}
	};

	private boolean backstackFragment() {

		if (mFragmentManager.getBackStackEntryCount() == 1) {
			this.finish();
		}

		else if (mFragmentManager.getBackStackEntryCount() == 2) {
			hideBackArrow();
			mFragmentManager.popBackStack();
			removeCurrentFragment();
		} else {
			mFragmentManager.popBackStack();
			removeCurrentFragment();
		}
		return false;
	}

	private void removeCurrentFragment() {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();

		Fragment currentFrag = mFragmentManager
				.findFragmentById(R.id.main_activity_container);

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

	private void initialize() {
		mActionBarArrow = (ImageView) findViewById(R.id.backArrow);
		mActionBarTitle = (TextView) findViewById(R.id.action_bar_title);
		mActionBarArrow.setOnClickListener(actionBarClickListener);

	}

	private void enableActionBarLeftArrow() {
		mActionBarArrow.setVisibility(View.VISIBLE);
		mActionBarArrow.bringToFront();
	}

	public void intializeJanrain(boolean isInitialized) {
		NetworkUtility.getInstance().checkIsOnline(getApplicationContext());
		IntentFilter flowFilter = new IntentFilter(
				Jump.JR_DOWNLOAD_FLOW_SUCCESS);
		flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
		LocalBroadcastManager.getInstance(this).registerReceiver(
				janrainStatusReceiver, flowFilter);
		if (NetworkUtility.getInstance().isOnline()) {
			JanrainConfigurationSettings user = JanrainConfigurationSettings
					.getInstance();
			// user.init(getApplicationContext(), PROD_CLIENT_ID,
			// MICROSITE_ID,REGISTRATION_USE_PROD, isInitialized, "en_US");
			user.init(getApplicationContext(), EVAL_CLIENT_Id, MICROSITE_ID,
					REGISTRATION_USE_EVAL, isInitialized, "en_US");
		} else {
			RLog.d(RLog.APPLICATION, "intializeJanrain : There is No internet");
		}
	}

}

package com.philips.cl.di.reg.ui.traditional;

import java.util.Locale;

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
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegistrationActivity extends FragmentActivity implements
		EventListener {

	private ImageView mActionBarArrow = null;
	private FragmentManager mFragmentManager = null;
	private TextView mActionBarTitle = null;
	private final String TAG = TextView.class.getSimpleName();

	public static boolean mJanrainIntialized = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NetworkUtility.getInstance().checkIsOnline(getApplicationContext());
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);

		setContentView(R.layout.activity_registration);
		mFragmentManager = getSupportFragmentManager();
		initialize();
		/** commented to restrict registration process */
		IntentFilter flowFilter = new IntentFilter(Jump.JR_DOWNLOAD_FLOW_SUCCESS);
		flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
		LocalBroadcastManager.getInstance(this).registerReceiver(janrainStatusReceiver, flowFilter);
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
				RLog.i(RLog.ACTIVITY_LIFECYCLE,
						"janrainStatusReceiver, intent = " + intent.toString());
				if ((Jump.JR_DOWNLOAD_FLOW_SUCCESS.equalsIgnoreCase(intent
						.getAction())) && (null != extras)) {
					mJanrainIntialized = true;
					System.out.println("success");
					
					EventHelper.getInstance().notifyEventOccurred(
							RegConstants.JANRAIN_INIT_SUCCESS);
				} else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW
						.equalsIgnoreCase(intent.getAction())
						&& (extras != null)) {
					
					System.out.println("failed in reciver");
					
				/*	EventHelper.getInstance().notifyEventOccurred(
							RegConstants.JANRAIN_INIT_FAILURE);*/
					mJanrainIntialized = false;
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

	/**
	 * Initialize Janrain
	 * 
	 * @param isInitialized
	 *            true for initialize and false for reinitialize Janrain
	 */
	public void intializeJanrain(boolean isInitialized) {
		if (NetworkUtility.getInstance().isOnline()) {
			JanrainConfigurationSettings user = JanrainConfigurationSettings
					.getInstance();

			user.init(getApplicationContext(), RegConstants.EVAL_CLIENT_Id,
					RegConstants.MICROSITE_ID,
					RegConstants.REGISTRATION_USE_EVAL, isInitialized, Locale
							.getDefault().toString());

			// user.init(getApplicationContext(), AppConstants.PROD_CLIENT_ID,
			// AppConstants.MICROSITE_ID, AppConstants.REGISTRATION_USE_PROD,
			// isInitialized,
			// LocaleUtil.getAppLocale(getApplicationContext()));
		}
	}

	public static boolean isJanrainIntialized() {
		return mJanrainIntialized;
	}

	public static void setJanrainIntialized(boolean janrainIntializationStatus) {
		mJanrainIntialized = janrainIntializationStatus;
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			if (!isJanrainIntialized()) {
				intializeJanrain(false);
			}
		}
	}

	@Override
	public void raiseEvent(String event) {
		// Do nothing
	}

}

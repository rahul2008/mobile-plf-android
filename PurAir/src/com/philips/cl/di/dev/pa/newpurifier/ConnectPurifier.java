package com.philips.cl.di.dev.pa.newpurifier;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.fragment.StartFlowChooseFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment.StartFlowListener;
import com.philips.cl.di.dev.pa.registration.UserRegistrationActivity;
import com.philips.cl.di.dev.pa.registration.UserRegistrationActivity.UserRegistrationChanged;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;

public class ConnectPurifier implements NetworkStateListener{

	private static ConnectPurifier mInstance;
	private StartFlowDialogFragment mDialog;
	private Bundle mBundle;
	private FragmentActivity mContext;
	private WifiManager mWifiManager;


	private ConnectPurifier(FragmentActivity context) {
		mContext = context;
		mDialog = new StartFlowDialogFragment();
		mDialog.setListener(mStartFlowListener);
		mBundle = new Bundle();

		mWifiManager = (WifiManager) mContext.getSystemService(MainActivity.WIFI_SERVICE);
	}

	public static synchronized ConnectPurifier getInstance(FragmentActivity context) {
		if (mInstance == null) {
			mInstance = new ConnectPurifier(context);
		}
		return mInstance;
	}

	/**
	 * start the flow to add a 
	 * - new purifier to your app
	 * - existing purifier to your app
	 */
	public void startAddPurifierToAppFlow() {

		ALog.i(ALog.APP_START_UP, "ConnectPurifier$startAddPurifierToAppFlow isWifiEnabled :: " + mWifiManager.isWifiEnabled() + " isUserSignedIn " + isUserSignedIn());

		if (!mWifiManager.isWifiEnabled()) {
			showNoWifiDialog();
		} else {
			if (!isUserSignedIn()) {
				startUserRegistrationFlow();
			} else {
				startChooseFragment();
			} 
		}
	}

	private void showNoWifiDialog() {
		ALog.i(ALog.APP_START_UP, "ConnectPurifier$showNoWifiDialog");
		try {
			mBundle.clear();
			mDialog = new StartFlowDialogFragment();
			mDialog.setListener(mStartFlowListener);
			mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, StartFlowDialogFragment.NO_WIFI);
			mDialog.setArguments(mBundle);
			mDialog.show(mContext.getSupportFragmentManager(), "start_flow_dialog");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void showNoInternetDialog() {
		ALog.i(ALog.APP_START_UP, "ConnectPurifier$showNoInternetDialog");
		try {
			mBundle.clear();
			mDialog = new StartFlowDialogFragment();
			mDialog.setListener(mStartFlowListener);
			mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, StartFlowDialogFragment.NO_INTERNET);
			mDialog.setArguments(mBundle);
			mDialog.show(mContext.getSupportFragmentManager(), "start_flow_dialog");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private boolean isUserSignedIn() {
		return UserRegistrationController.getInstance().isUserLoggedIn();
	}

	private void startUserRegistrationFlow() {
		ALog.i(ALog.APP_START_UP, "ConnectPurifier$startUserRegistrationFlow");
		Intent intent = new Intent(mContext, UserRegistrationActivity.class);
		mContext.startActivity(intent);

		UserRegistrationActivity.setUserRegistrationChangedListener(mUserRegistrationChangedListener);
	}

	private void startChooseFragment() {
		ALog.i(ALog.APP_START_UP, "ConnectPurifier$startChooseFragment mContext " + mContext);
		((MainActivity) mContext).showFragment(new StartFlowChooseFragment());
	}

	private StartFlowListener mStartFlowListener = new StartFlowListener() {

		@Override
		public void noWifiTurnOnClicked(DialogFragment dialog) {
			if (!isUserSignedIn()) {
//				NetworkReceiver.getInstance().removeNetworkStateListener(ConnectPurifier.this);
//				NetworkReceiver.getInstance().addNetworkStateListener(ConnectPurifier.this);
			} else {
				startChooseFragment();
			}
		}

		@Override
		public void noInternetTurnOnClicked(DialogFragment dialog) {
			startUserRegistrationFlow();
		}

		@Override
		public void locationServiceAllowClicked(DialogFragment dialog) {
		}

		@Override
		public void locationServiceTurnOnClicked(DialogFragment dialog) {
		}

		@Override
		public void dialogCancelClicked(DialogFragment dialog) {
		}

		@Override
		public void onPurifierSelect(PurAirDevice purifier) {
		}
	};

	private UserRegistrationChanged mUserRegistrationChangedListener = new UserRegistrationChanged() {

		@Override
		public void userRegistrationClosed(boolean firstUse) {
			if (firstUse) {
				startChooseFragment();
			}
		}
	};

	public static void reset() {
		mInstance = null ;
	}

	@Override
	public void onConnected(String ssid) {
		Intent intent = new Intent(mContext, UserRegistrationActivity.class);
		mContext.startActivity(intent);

//		UserRegistrationActivity.setUserRegistrationChangedListener(mUserRegistrationChangedListener);
//		NetworkReceiver.getInstance().removeNetworkStateListener(this);
	}

	@Override
	public void onDisconnected() {
		showNoInternetDialog();
//		NetworkReceiver.getInstance().removeNetworkStateListener(this);
	}	
}

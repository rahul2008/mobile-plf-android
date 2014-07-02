package com.philips.cl.di.dev.pa.newpurifier;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.fragment.StartFlowChooseFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment.StartFlowListener;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkChangedCallback;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.registration.UserRegistrationActivity;
import com.philips.cl.di.dev.pa.registration.UserRegistrationActivity.UserRegistrationChanged;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;

public class ConnectPurifier {

	private static ConnectPurifier mInstance;
	private StartFlowDialogFragment mDialog;
	private Bundle mBundle;
	private FragmentActivity mContext;
	private WifiManager mWifiManager;
	private NetworkState mLastKnownNetworkState = NetworkState.NONE;
	
	
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
		mBundle.clear();
		mDialog = new StartFlowDialogFragment();
		mDialog.setListener(mStartFlowListener);
		mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, StartFlowDialogFragment.NO_WIFI);
		mDialog.setArguments(mBundle);
		mDialog.show(mContext.getSupportFragmentManager(), "start_flow_dialog");
	}
	
	private void showNoInternetDialog() {
		mBundle.clear();
		mDialog = new StartFlowDialogFragment();
		mDialog.setListener(mStartFlowListener);
		mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, StartFlowDialogFragment.NO_INTERNET);
		mDialog.setArguments(mBundle);
		mDialog.show(mContext.getSupportFragmentManager(), "start_flow_dialog");
	}
	
	private boolean isUserSignedIn() {
		return UserRegistrationController.getInstance().isUserLoggedIn();
	}
	
	private boolean isInternetConnected() {
		NetworkMonitor monitor = new NetworkMonitor(mContext, mNetworkChangedListener);
		mLastKnownNetworkState = monitor.getLastKnownNetworkState();
		
		if (mLastKnownNetworkState == NetworkState.MOBILE || mLastKnownNetworkState == NetworkState.WIFI_WITH_INTERNET) {
			return true;
		} else {
			return false; 
		}
	}
	
	private void startUserRegistrationFlow() {
		
		if (isInternetConnected()) {
			Intent intent = new Intent(mContext, UserRegistrationActivity.class);
			mContext.startActivity(intent);
			
			UserRegistrationActivity.setUserRegistrationChangedListener(mUserRegistrationChangedListener);
		} else {
			showNoInternetDialog();
		}
	}
	
	private void startChooseFragment() {
		mContext.getSupportFragmentManager().beginTransaction()
		.replace(R.id.llContainer, new StartFlowChooseFragment())
		.addToBackStack(null)
		.commitAllowingStateLoss();
	}
		
	private StartFlowListener mStartFlowListener = new StartFlowListener() {

		@Override
		public void noWifiTurnOnClicked(DialogFragment dialog) {
			if (!isUserSignedIn()) {
				startUserRegistrationFlow();
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
	};

	private NetworkChangedCallback mNetworkChangedListener = new NetworkChangedCallback() {
		
		@Override
		public void onNetworkChanged(NetworkState networkState, String networkSsid) {
			mLastKnownNetworkState = networkState;
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
	
}

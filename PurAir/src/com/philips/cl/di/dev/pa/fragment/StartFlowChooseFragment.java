package com.philips.cl.di.dev.pa.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.dashboard.GPSLocation;
import com.philips.cl.di.dev.pa.demo.DemoModeTask;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment.StartFlowListener;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class StartFlowChooseFragment extends BaseFragment implements
		OnClickListener, StartFlowListener, ServerResponseListener {

	private Button mBtnNewPurifier;
	private Button mBtnConnectedPurifier;
	private Bundle mBundle;
	private StartFlowDialogFragment mDialog;
	private PurAirDevice selectedPurifier;
	private DemoModeTask connectTask;
	private GPSLocation gpsLocation;
	private LocationManager locationManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.start_flow_choose_fragment,
				container, false);

		mBundle = new Bundle();
		mDialog = new StartFlowDialogFragment();

		mBtnNewPurifier = (Button) view
				.findViewById(R.id.start_flow_choose_btn_connect_new);
		mBtnConnectedPurifier = (Button) view
				.findViewById(R.id.start_flow_choose_btn_already_connected);

		mBtnNewPurifier.setOnClickListener(this);
		mBtnConnectedPurifier.setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		locationManager = (LocationManager) PurAirApplication.getAppContext()
				.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		 * if (isGPSEnabled()) { gpsLocation = GPSLocation.getInstance();
		 * ALog.i(ALog.OUTDOOR_LOCATION, "gpsLocation: " + gpsLocation); } else
		 * { showEnableGPSDialog(); }
		 */
	}

	private boolean isGPSEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	private void startEWS() {
		Intent intent = new Intent(getActivity(), EWSActivity.class);
		getActivity().startActivity(intent);
		getFragmentManager().popBackStackImmediate();
	}

	private void showApSelectorDialog() {
		try {
			mBundle.clear();
			mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER,
					StartFlowDialogFragment.AP_SELCTOR);
			mDialog.setArguments(mBundle);
			mDialog.setListener(this);
			mDialog.show(getFragmentManager(), "start_flow_dialog");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_flow_choose_btn_connect_new:
			startEWS();
			break;
		case R.id.start_flow_choose_btn_already_connected:
			DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
			final List<PurAirDevice> apItems = discoveryManager
					.getNewDevicesDiscovered();
			if (apItems.size() > 0) {
				showApSelectorDialog();
			} else {
				showAlertDialog("", getString(R.string.no_purifier_found));
				// TODO show troubleshoot flow
			}
			break;
		default:
			break;
		}
	}

	private void showEnableGPSDialog() {
		if (getActivity() == null)
			return;
		try {
			FragmentTransaction fragTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = getActivity().getSupportFragmentManager()
					.findFragmentByTag("gps_enable");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}

			fragTransaction.add(GPSLocationDialogFragment.newInstance(),
					"gps_enable").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, e.getMessage());
		}
	}

	private void showAlertDialog(String title, String message) {
		if (getActivity() == null)
			return;
		try {
			FragmentTransaction fragTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = getActivity().getSupportFragmentManager()
					.findFragmentByTag("no_purifier_found");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}

			fragTransaction.add(
					DownloadAlerDialogFragement.newInstance(title, message),
					"no_purifier_found").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, e.getMessage());
		}
	}

	private CountDownTimer connectTimer = new CountDownTimer(30000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			if (connectTask != null) {
				connectTask.stopTask();
			}
			showErrorOnConnectPurifier();
		}
	};

	private void showErrorOnConnectPurifier() {
		SetupDialogFactory.getInstance(getActivity()).dismissSignalStrength();
		showAlertDialog(getString(R.string.purifier_add_fail_title),
				getString(R.string.purifier_add_fail_msg));
	}

	private void stopSSIDTimer() {
		if (connectTimer != null) {
			connectTimer.cancel();
		}
	}

	private void getWifiDetails() {
		ALog.i(ALog.MANAGE_PUR, "gettWifiDetails");

		if (selectedPurifier == null)
			return;
		connectTimer.start();
		connectTask = new DemoModeTask(this, Utils.getPortUrl(Port.WIFI,
				selectedPurifier.getIpAddress()), "", "GET");
		connectTask.start();
	}

	private void onSuccessfullyConnected() {
		SetupDialogFactory.getInstance(getActivity()).dismissSignalStrength();
		if (selectedPurifier != null) {
			selectedPurifier
					.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
			selectedPurifier.setLastKnownNetworkSsid(EWSWifiManager
					.getSsidOfConnectedNetwork());
			PurifierManager.getInstance().setCurrentPurifier(selectedPurifier);

			((MainActivity) getActivity())
					.setTitle(getString(R.string.congratulations));
			((MainActivity) getActivity())
					.showFragment(new CongratulationFragment());

			// Utils.saveAppFirstUse(false);
			//
			// PurifierDatabase purifierDatabase = new PurifierDatabase();
			// purifierDatabase.insertPurAirDevice(selectedPurifier);
			// List<PurAirDevice> purifiers =
			// DiscoveryManager.getInstance().updateStoreDevices();
			// PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(purifiers.size()
			// - 1);
		} else {
			showAlertDialog(getString(R.string.purifier_add_fail_title),
					getString(R.string.purifier_add_fail_msg));
		}
	}

	@Override
	public void onPurifierSelect(PurAirDevice purifier) {
		SetupDialogFactory.getInstance(getActivity()).cleanUp();
		selectedPurifier = purifier;
		SetupDialogFactory.getInstance(getActivity()).dismissSignalStrength();
		SetupDialogFactory.getInstance(getActivity()).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		getWifiDetails();
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData,
			final String fromIp) {

		final String decryptedResponse = new DISecurity(null).decryptData(
				responseData, selectedPurifier);

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (decryptedResponse != null) {
						ALog.i(ALog.MANAGE_PUR, decryptedResponse);

						stopSSIDTimer();
						onSuccessfullyConnected();
					}
				}
			});
		}
	}

	@Override
	public void noWifiTurnOnClicked(DialogFragment dialog) {
		// TODO Auto-generated method stub
	}

	@Override
	public void noInternetTurnOnClicked(DialogFragment dialog) {
		// TODO Auto-generated method stub
	}

	@Override
	public void locationServiceAllowClicked(DialogFragment dialog) {
		// TODO Auto-generated method stub
	}

	@Override
	public void locationServiceTurnOnClicked(DialogFragment dialog) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dialogCancelClicked(DialogFragment dialog) {
		// TODO Auto-generated method stub
	}
}

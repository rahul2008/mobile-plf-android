package com.philips.cl.di.dev.pa.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.dashboard.GPSLocation;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;

public class StartFlowDialogFragment extends DialogFragment {

	public static final String DIALOG_NUMBER = "dialog_number";
	public static final int NO_INTERNET = 0;
	public static final int LOCATION_SERVICES = 1;
	public static final int LOCATION_SERVICES_TURNED_OFF = 2;
	public static final int NO_WIFI = 4;
	public static final int AP_SELCTOR = 5;
	public static final int SEARCHING = 6;

	private WifiManager mWifiManager;

	private int mSelectedDialogNumber = 0;
	private StartFlowListener mListener;

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		mSelectedDialogNumber = bundle.getInt(DIALOG_NUMBER, 0);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		switch (mSelectedDialogNumber) {
		case NO_INTERNET:
			builder = createNoInternetDialog(builder);
			break;
		case LOCATION_SERVICES:
			builder = createLocationServicesDialog(builder);
			break;
		case LOCATION_SERVICES_TURNED_OFF:
			builder = createLocationServicesTurnedOffDialog(builder);
			break;
		case NO_WIFI:
			builder = createNoWifiDialog(builder);
			break;
		case SEARCHING:
			builder = createSearchingDialog(builder);
			break;
		default:
			break;
		}

		return builder.create();
	}

	public void setListener(StartFlowListener listener) {
		mListener = listener;
	}

	private AlertDialog.Builder createNoInternetDialog(
			AlertDialog.Builder builder) {
		builder.setTitle(R.string.no_internet_title)
				.setMessage(R.string.no_internet_text)
				.setPositiveButton(R.string.turn_it_on,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mWifiManager = (WifiManager) getActivity()
										.getSystemService(MainActivity.WIFI_SERVICE);
								mWifiManager.setWifiEnabled(true);
								dismiss();
								mListener.noInternetTurnOnClicked(StartFlowDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
								mListener.dialogCancelClicked(StartFlowDialogFragment.this);
							}
						});
		return builder;
	}

	private AlertDialog.Builder createLocationServicesTurnedOffDialog(
			AlertDialog.Builder builder) {
		builder.setTitle(R.string.location_services_turned_off_title)
				.setMessage(R.string.location_services_turned_off_text)
				.setPositiveButton(R.string.turn_it_on,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Enable device location service window since
								// android doesn't allow to change location
								// settings in code
								Intent myIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(myIntent);
								dismiss();
								mListener.locationServiceTurnOnClicked(StartFlowDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
								mListener.dialogCancelClicked(StartFlowDialogFragment.this);
							}
						});
		return builder;
	}

	private AlertDialog.Builder createLocationServicesDialog(
			AlertDialog.Builder builder) {
		builder.setTitle(R.string.location_services_title)
				.setMessage(R.string.location_services_text)
				.setPositiveButton(R.string.allow,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (!GPSLocation.getInstance().isGPSEnabled()) {
									// If GPS is not enabled, request user to
									// enable location services here.
								}
								dismiss();
								mListener.locationServiceAllowClicked(StartFlowDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.do_not_allow,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
								mListener.dialogCancelClicked(StartFlowDialogFragment.this);
							}
						});
		return builder;
	}

	private AlertDialog.Builder createNoWifiDialog(AlertDialog.Builder builder) {
		builder.setTitle(R.string.no_wifi_title)
				.setMessage(R.string.no_wifi_text)
				.setPositiveButton(R.string.turn_it_on,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mWifiManager = (WifiManager) getActivity()
										.getSystemService(MainActivity.WIFI_SERVICE);
								mWifiManager.setWifiEnabled(true);
								dialog.dismiss();
								mListener.noWifiTurnOnClicked(StartFlowDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
								mListener.dialogCancelClicked(StartFlowDialogFragment.this);
							}
						});
		return builder;
	}


	private AlertDialog.Builder createSearchingDialog(
			AlertDialog.Builder builder) {
		builder.setMessage(R.string.searching).setNegativeButton(
				R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
						mListener.dialogCancelClicked(StartFlowDialogFragment.this);
					}
				});
		return builder;
	}

	public interface StartFlowListener {
		void noWifiTurnOnClicked(DialogFragment dialog);

		void noInternetTurnOnClicked(DialogFragment dialog);

		void locationServiceAllowClicked(DialogFragment dialog);

		void locationServiceTurnOnClicked(DialogFragment dialog);

		void dialogCancelClicked(DialogFragment dialog);

		void onPurifierSelect(PurAirDevice purifier);
	}

}

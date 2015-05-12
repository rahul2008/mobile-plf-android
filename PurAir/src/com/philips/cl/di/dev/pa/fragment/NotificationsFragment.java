package com.philips.cl.di.dev.pa.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.DICommPortListener;
import com.philips.cl.di.dicomm.port.ListenerRegistration;

public class NotificationsFragment extends BaseFragment implements
OnCheckedChangeListener, PermissionListener, AirPurifierEventListener,
android.widget.RadioGroup.OnCheckedChangeListener,
AlertDialogBtnInterface, OnClickListener {

	private RelativeLayout enableLayout;
	private LinearLayout detailedLayout;

	private ToggleButton notificationToggle;
	private LinearLayout indoorAqiLbls;
	private RadioGroup indoorAqiRadioBtns;
	private FontTextView radioButtonLable0, radioButtonLable1,
	radioButtonLable2, radioButtonLable3;

	private PairingHandler pairingHandler;
	private ProgressDialog aqiThresholdProgressDialog;
	private ProgressDialog getRelationProgressDialog;

	private AirPurifier mPurifier;

	private String aqiThreshold;
	private static final int AQI_THRESHOLD_TIMEOUT = 120 * 1000;
	public static final String NOTIFICATION_PROGRESS_DIALOG="pairing_dialog";

	private ViewGroup lastConnectionLL;
	private FontTextView lastConnectionTimeTV;
	
	private DICommPortListener mAirPortListener = new DICommPortListener() {
		@Override
		public ListenerRegistration onPortUpdate(DICommPort<?> port) {
			//TODO:DICOMM Refactor, define new method after purifiereventlistener is removed
			updateUI();
            return ListenerRegistration.KEEP_REGISTERED;
		}

        @Override
        public ListenerRegistration onPortError(DICommPort<?> port, Error error, String errorData) {
            //TODO:DICOMM Refactor, define new method after purifiereventlistener is removed
            handleSetThresholdError(error);
            return ListenerRegistration.KEEP_REGISTERED;
        }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		pairingHandler = new PairingHandler(null, mPurifier);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notifications_fragment, container, false);
		initializeAllViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity parent = this.getActivity();
		if (parent == null)	return;

		MetricsTracker.trackPage(TrackPageConstants.PUSH_NOTIFICATION);
//		ALog.i(ALog.NOTIFICATION, "Right menu icon is orange "
//				+ ((MainActivity) parent).getRightMenuDisconnectionState());
		if (mPurifier == null
				|| mPurifier.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED
				/*|| ((MainActivity) parent).getRightMenuDisconnectionState()*/) {

			try {
				AlertDialogFragment dialog = AlertDialogFragment.newInstance(
						R.string.notification_nopurifier_title,
						R.string.notification_nopurifier_text,
						R.string.notification_nopurifier_positivebtn);

				dialog.setOnClickListener(this);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				fm.beginTransaction().add(dialog, null).commitAllowingStateLoss();
			} catch (IllegalStateException e) {
				ALog.e(ALog.NOTIFICATION, "Error: " + e.getMessage());
			}
			return;
		}
		AirPurifierManager.getInstance().addAirPurifierEventListener(this);
		pairingHandler.setPermissionListener(this);

		if (mPurifier == null || PurAirApplication.isDemoModeEnable()) {
			disableNotificationScreen();
		} else if (mPurifier.getNetworkNode().getPairedState() == NetworkNode.PAIRED_STATUS.PAIRED) {
			getNotificationPermission();
			showNotificationsLayout();
		} else {
			if(mPurifier.getNetworkNode().getPairedState()!=NetworkNode.PAIRED_STATUS.PAIRED){
				showPairingProgressDialog();
			}
			if(mPurifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED){
				pairingHandler.resetPairingAttempts(mPurifier.getNetworkNode().getCppId());
				((MainActivity)getActivity()).pairToPurifierIfNecessary();
			}
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		AirPurifier currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier!=null){
		    currentPurifier.getAirPort().registerPortListener(mAirPortListener);
		}
	}
	
	@Override
	public void onPause() {
		AirPurifier currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier!=null){
		    currentPurifier.getAirPort().unregisterPortListener(mAirPortListener);
		}
		super.onPause();
	}

	private void showPairingProgressDialog(){
		if (getActivity() == null) return;
		Fragment progressDialog= ProgressDialogFragment.newInstance(getString(R.string.please_wait));
		try {
			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.add(progressDialog, NOTIFICATION_PROGRESS_DIALOG);
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.NOTIFICATION, "Error: " + e.getMessage());
		}
	}

	private void initializeAllViews(View rootView) {
		ImageButton backButton = (ImageButton) rootView.findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		FontTextView heading=(FontTextView) rootView.findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.list_item_notifications));
		lastConnectionLL = (LinearLayout) rootView.findViewById(R.id.notifications_last_connection_ll);
		lastConnectionLL.setVisibility(View.GONE);
		lastConnectionTimeTV = (FontTextView) rootView.findViewById(R.id.notifications_last_connection_time_tv);
		enableLayout = (RelativeLayout) rootView.findViewById(R.id.notifications_enable_layout);
		detailedLayout = (LinearLayout) rootView.findViewById(R.id.notifications_detailed_layout);

		TextView enableText = (TextView) rootView.findViewById(R.id.notifications_enable_all_text);
		String purifierName = (mPurifier == null ? "" : mPurifier.getName());
		enableText.setText(purifierName);

		notificationToggle = (ToggleButton) rootView.findViewById(R.id.notifications_enable_all_toggle);

		indoorAqiLbls = (LinearLayout) rootView.findViewById(R.id.notifications_indoor_aqi_lbls);
		indoorAqiRadioBtns = (RadioGroup) rootView.findViewById(R.id.notifications_indoor_radioGroup);
		radioButtonLable0 = (FontTextView) rootView.findViewById(R.id.notifications_indoor_label0);
		radioButtonLable1 = (FontTextView) rootView.findViewById(R.id.notifications_indoor_label1);
		radioButtonLable2 = (FontTextView) rootView.findViewById(R.id.notifications_indoor_label2);
		radioButtonLable3 = (FontTextView) rootView.findViewById(R.id.notifications_indoor_label3);
	}

	private void setUIAqiThreshold(int aqiThreshold) {
		MetricsTracker.trackActionNotificationAirQuality("NotificationAirQuality " + aqiThreshold);
		switch (aqiThreshold) {
		case 13:
			RadioButton aqiRadioButton0 = (RadioButton) indoorAqiRadioBtns.getChildAt(0);
			aqiRadioButton0.setChecked(true);
			highLightLabel(AppConstants.INDEX_0);
			break;
		case 19:
			RadioButton aqiRadioButton1 = (RadioButton) indoorAqiRadioBtns.getChildAt(1);
			aqiRadioButton1.setChecked(true);
			highLightLabel(AppConstants.INDEX_1);
			break;
		case 29:
			RadioButton aqiRadioButton2 = (RadioButton) indoorAqiRadioBtns.getChildAt(2);
			aqiRadioButton2.setChecked(true);
			highLightLabel(AppConstants.INDEX_2);
			break;
		case 40:
			RadioButton aqiRadioButton3 = (RadioButton) indoorAqiRadioBtns.getChildAt(3);
			aqiRadioButton3.setChecked(true);
			highLightLabel(AppConstants.INDEX_3);
			break;
		default:
			highLightLabel(-1);
			break;
		}
	}

	private void highLightLabel(int position) {
		switch (position) {
		case AppConstants.INDEX_0:
			radioButtonLable0.setTypeface(null, Typeface.BOLD);
			radioButtonLable1.setTypeface(null, Typeface.NORMAL);
			radioButtonLable2.setTypeface(null, Typeface.NORMAL);
			radioButtonLable3.setTypeface(null, Typeface.NORMAL);
			break;
		case AppConstants.INDEX_1:
			radioButtonLable0.setTypeface(null, Typeface.NORMAL);
			radioButtonLable1.setTypeface(null, Typeface.BOLD);
			radioButtonLable2.setTypeface(null, Typeface.NORMAL);
			radioButtonLable3.setTypeface(null, Typeface.NORMAL);
			break;
		case AppConstants.INDEX_2:
			radioButtonLable0.setTypeface(null, Typeface.NORMAL);
			radioButtonLable1.setTypeface(null, Typeface.NORMAL);
			radioButtonLable2.setTypeface(null, Typeface.BOLD);
			radioButtonLable3.setTypeface(null, Typeface.NORMAL);
			break;
		case AppConstants.INDEX_3:
			radioButtonLable0.setTypeface(null, Typeface.NORMAL);
			radioButtonLable1.setTypeface(null, Typeface.NORMAL);
			radioButtonLable2.setTypeface(null, Typeface.NORMAL);
			radioButtonLable3.setTypeface(null, Typeface.BOLD);
			break;
		default:
			radioButtonLable0.setTypeface(null, Typeface.NORMAL);
			radioButtonLable1.setTypeface(null, Typeface.NORMAL);
			radioButtonLable2.setTypeface(null, Typeface.NORMAL);
			radioButtonLable3.setTypeface(null, Typeface.NORMAL);
			break;
		}
	}

	private void showNotificationsLayout() {
		enableLayout.setVisibility(View.VISIBLE);
		enableDetailedNotificationsLayout();
	}

	@SuppressLint("NewApi")
	private void enableDetailedNotificationsLayout() {
		indoorAqiLbls.setVisibility(View.VISIBLE);
		indoorAqiRadioBtns.setVisibility(View.VISIBLE);

		if (Build.VERSION.SDK_INT < 11) {
			final AlphaAnimation animation = new AlphaAnimation(0.5f, 1f);
			animation.setDuration(500);
			animation.setFillAfter(true);
			detailedLayout.startAnimation(animation);
		} else {
			detailedLayout.setAlpha(1f);
		}
	}

	@SuppressLint("NewApi")
	private void disableDetailedNotificationsLayout() {
		indoorAqiLbls.setVisibility(View.GONE);
		indoorAqiRadioBtns.setVisibility(View.GONE);

		if (Build.VERSION.SDK_INT < 11) {
			final AlphaAnimation animation = new AlphaAnimation(1f, 0.5f);
			animation.setDuration(500);
			animation.setFillAfter(true);
			detailedLayout.startAnimation(animation);
		} else {
			detailedLayout.setAlpha(0.5f);
		}

	}

	private void getNotificationPermission() {
		NotificationRegisteringManager.getNotificationManager().registerAppForNotification();

		if (mPurifier != null && mPurifier.getNetworkNode().getPairedState() == NetworkNode.PAIRED_STATUS.PAIRED) {

			showGetRelationProgressDialog(R.string.notification_permission_check_msg);

			// Enable UI and check if permission exists
			pairingHandler.getPermission(
					PairingHandler.PAIRING_NOTIFY_RELATIONSHIP,
					PairingHandler.PAIRING_PUSH_PERMISSIONS
					.toArray(new String[PairingHandler.PAIRING_PUSH_PERMISSIONS.size()]));
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		System.out.println("manzer Toggle checked");
		if (mPurifier != null && ConnectionState.DISCONNECTED == mPurifier.getNetworkNode().getConnectionState()) {
			return;
		}
		switch (button.getId()) {
		case R.id.notifications_enable_all_toggle:
			if (isChecked) {
				showProgressDialog(R.string.notification_enabling_msg);
				enableDetailedNotificationsLayout();
				pairingHandler.addPermission(
						PairingHandler.PAIRING_NOTIFY_RELATIONSHIP,
						PairingHandler.PAIRING_PUSH_PERMISSIONS
						.toArray(new String[PairingHandler.PAIRING_PUSH_PERMISSIONS.size()]));
			} else {
				showProgressDialog(R.string.notification_disabling_msg);
				pairingHandler.removePermission(
						PairingHandler.PAIRING_NOTIFY_RELATIONSHIP,
						PairingHandler.PAIRING_PUSH_PERMISSIONS
						.toArray(new String[PairingHandler.PAIRING_PUSH_PERMISSIONS.size()]));
				disableDetailedNotificationsLayout();
			}
			MetricsTracker.trackActionNotification(isChecked);
			break;
		default:
			break;
		}
	}

	private void setAQIThreshold(String aqiThreshold) {
		ALog.i(ALog.NOTIFICATION, "Setting AQIThreshold: " + aqiThreshold);
		if (mPurifier == null) return;
		aqiThresholdTimer.start();
		showProgressDialog(R.string.notification_send_aqi_level_msg);
		
		AirPurifier currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier!=null){
			currentPurifier.getAirPort().putProperties(ParserConstants.AQI_THRESHOLD, aqiThreshold);
		}
	}

	private CountDownTimer aqiThresholdTimer = new CountDownTimer(
			AQI_THRESHOLD_TIMEOUT, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			if (aqiThresholdProgressDialog != null) aqiThresholdProgressDialog.dismiss();
			showError();
		}
	};

	@Override
	public void onPermissionReturned(final boolean permissionExists) {
		dismissAllProgessDialog();
		if (getActivity() == null)	return;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notificationToggle.setEnabled(true);
				if (permissionExists) {
					notificationToggleChecked(true);
					enableDetailedNotificationsLayout();
				} else {
					notificationToggleChecked(false);
					disableDetailedNotificationsLayout();
				}
				if (mPurifier != null && mPurifier.getAirPort().getPortProperties() != null) {
					setUIAqiThreshold(mPurifier.getAirPort().getPortProperties().getAqiThreshold());
				}
				indoorAqiRadioBtns.setOnCheckedChangeListener(NotificationsFragment.this);
			}
		});
	}

	@Override
	public void onPermissionRemoved() {
		ALog.i(ALog.NOTIFICATION, "Permission removed");
		if (getActivity() == null)	return;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (aqiThresholdProgressDialog != null) aqiThresholdProgressDialog.dismiss();
			}
		});
	}

	@Override
	public void onPermissionAdded() {
		ALog.i(ALog.NOTIFICATION, "Permission added");
		if (getActivity() == null)	return;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (aqiThresholdProgressDialog != null) aqiThresholdProgressDialog.dismiss();
			}
		});
	}

	@Override
	public void onCallFailed() {
		ALog.i(ALog.NOTIFICATION, "Failed to change permissions");
		if (getActivity() == null) return;

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dismissAllProgessDialog();
				try {
					AlertDialogFragment dialog = AlertDialogFragment.newInstance(
							R.string.error_title,
							R.string.notification_error_msg,
							R.string.notification_nopurifier_positivebtn);
					dialog.setOnClickListener(NotificationsFragment.this);
					FragmentManager fm = getActivity().getSupportFragmentManager();
					fm.beginTransaction().add(dialog, null).commitAllowingStateLoss();
				} catch (IllegalStateException e) {
					ALog.e(ALog.NOTIFICATION, "Error: " + e.getMessage());
				}
			}
		});
	}

	@Override
	public void onPositiveButtonClicked() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;

		((MainActivity) parent).onBackPressed();
	}

	/**
	 * This method is called if the call to set AQI threshold via locally fails
	 */
	public void handleSetThresholdError(Error error) {
		// TODO:DICOMM Refactor, check error mapping
		if (aqiThresholdTimer != null) aqiThresholdTimer.cancel();
		if (aqiThresholdProgressDialog != null) aqiThresholdProgressDialog.dismiss();

		MetricsTracker.trackActionTechnicalError("Notification enabling failed");
		showError();
	}

	public void updateUI() {
		//		ALog.i(ALog.NOTIFICATION, "aqi threshold added");
		if (mPurifier != null && ConnectionState.DISCONNECTED != mPurifier.getNetworkNode().getConnectionState()) {
			lastConnectionLL.setVisibility(View.GONE);
		}
		if (aqiThresholdProgressDialog != null)	aqiThresholdProgressDialog.dismiss();
		if (aqiThresholdTimer != null) aqiThresholdTimer.cancel();

	}
	

	@Override
	public void onNegativeButtonClicked() {
		// NOP
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.notifications_indoor_radio0:
			aqiThreshold = "13";
			highLightLabel(AppConstants.INDEX_0);
			break;
		case R.id.notifications_indoor_radio1:
			aqiThreshold = "19";
			highLightLabel(AppConstants.INDEX_1);
			break;
		case R.id.notifications_indoor_radio2:
			aqiThreshold = "29";
			highLightLabel(AppConstants.INDEX_2);
			break;
		case R.id.notifications_indoor_radio3:
			aqiThreshold = "40";
			highLightLabel(AppConstants.INDEX_3);
			break;
		default:
			break;
		}
		setAQIThreshold(aqiThreshold);
	}

	private void showProgressDialog(int msg) {
		if (getActivity() == null)	return;
		dismissAllProgessDialog();
		if (aqiThresholdProgressDialog == null) {
			aqiThresholdProgressDialog = new ProgressDialog(getActivity());
		}

		aqiThresholdProgressDialog.setMessage(getString(msg));
		aqiThresholdProgressDialog.setCancelable(false);
		aqiThresholdProgressDialog.show();
	}

	private void showGetRelationProgressDialog(int msg) {
		if (getActivity() == null)	return;
		dismissAllProgessDialog();
		if (getRelationProgressDialog == null) {
			getRelationProgressDialog = new ProgressDialog(getActivity());
		}

		getRelationProgressDialog.setMessage(getString(msg));
		getRelationProgressDialog.setCancelable(false);
		getRelationProgressDialog.show();
	}

	private void dismissAllProgessDialog() {
		if (getRelationProgressDialog != null) {
			getRelationProgressDialog.dismiss();
		}
		if (aqiThresholdProgressDialog != null) {
			aqiThresholdProgressDialog.dismiss();
		}
	}

	private void showError() {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showLastConnectionAlert();
				}
			});
		}
	}

	private void updateUIConnectionState() {
		if (getActivity() == null)	return;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mPurifier == null) return;
				if (ConnectionState.DISCONNECTED == mPurifier.getNetworkNode().getConnectionState()) {
					showLastConnectionAlert();
					disableNotificationScreen();
				} else if (NetworkNode.PAIRED_STATUS.PAIRED == mPurifier.getNetworkNode().getPairedState()){
					lastConnectionLL.setVisibility(View.GONE);
					getNotificationPermission();
				}
			}
		});
	}

	private void showLastConnectionAlert() {
		lastConnectionLL.setVisibility(View.VISIBLE);
		lastConnectionTimeTV.setText(DashboardUtil.getCurrentTime24HrFormat());
	}

	@Override
	public void onClick(View view) {
		//Back button
		if (view.getId() == R.id.heading_back_imgbtn) {
			MainActivity mainActivity = (MainActivity) getActivity();
			if (mainActivity != null) {
				mainActivity.onBackPressed();
			}
		}

	}

	private void notificationToggleChecked(boolean checked) {
		// Added this line, to avoid to call setOnCheckedChangeListener without user toggle button press.
		notificationToggle.setOnCheckedChangeListener(null);
		notificationToggle.setChecked(checked);
		notificationToggle.setOnCheckedChangeListener(this);
	}

	// After onPairingSuccess we need change layout visibility
	public void refreshNotificationLayout() {
		getNotificationPermission();
	}

	public void disableNotificationScreen() {
		notificationToggleChecked(false);
		notificationToggle.setEnabled(false);
		disableDetailedNotificationsLayout();
	}

	@Override
	public void onAirPurifierChanged() {
		updateUIConnectionState();
	}

	@Override
	public void onAirPurifierEventReceived() {
		// TODO DIComm refactor remove when changing AirpurifierEventListener to CurrentPurifierListener
	}

	@Override
	public void onFirmwareEventReceived() {
		// TODO DIComm refactor remove when changing AirpurifierEventListener to CurrentPurifierListener
	}

	@Override
	public void onErrorOccurred(Error purifierEventError) {
		// TODO DIComm refactor remove when changing AirpurifierEventListener to CurrentPurifierListener
	}
}

package com.philips.cl.di.dev.pa.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;

public class NotificationsFragment extends BaseFragment implements OnCheckedChangeListener, PermissionListener, AirPurifierEventListener, android.widget.RadioGroup.OnCheckedChangeListener, AlertDialogBtnInterface {

	private RelativeLayout pairingLayout;
	private RelativeLayout enableLayout;
	private LinearLayout detailedLayout;
	
	private Button pairingButton;
	private ToggleButton notificationToggle;
	private LinearLayout indoorAqiLbls;
	private RadioGroup indoorAqiRadioBtns;
	
	private PairingHandler pairingHandler;
	private ProgressDialog progressDialog;
	
	private PurAirDevice mPurifier;

	private String aqiThreshold;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPurifier = ((MainActivity) getActivity()).getCurrentPurifier();
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
		
		if (mPurifier == null || mPurifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			Activity parent = this.getActivity();
			if (parent != null && parent instanceof MainActivity) {
				AlertDialogFragment dialog = AlertDialogFragment.newInstance(R.string.notification_nopurifier_title, R.string.notification_nopurifier_text, R.string.notification_nopurifier_positivebtn);
				dialog.setOnClickListener(this);
				dialog.show(((MainActivity) parent).getSupportFragmentManager(), null);
			}
		}
		PurifierManager.getInstance().addAirPurifierEventListener(this);
		pairingHandler.setPermissionListener(this);
		
		boolean notificationsEnabled = true; // TODO replace by actual pairing check
		if (mPurifier == null) {
			showNotificationsLayout(false);
		} else if (mPurifier.isPaired()) {
			notificationSetup();
			showNotificationsLayout(notificationsEnabled);
		} else {
			showPairingLayout();
		}
	}
	
	
	@Override
	public void onPause() {
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		pairingHandler.setPermissionListener(null);
		super.onPause();
	}
	
	private void initializeAllViews(View rootView) {
		pairingLayout = (RelativeLayout) rootView.findViewById(R.id.notifications_pairing_layout);
		enableLayout = (RelativeLayout) rootView.findViewById(R.id.notifications_enable_layout);
		detailedLayout = (LinearLayout) rootView.findViewById(R.id.notifications_detailed_layout);
		
		pairingButton = (Button) rootView.findViewById(R.id.btn_notifications_pairing);
		pairingButton.setTypeface(Fonts.getGillsans(getActivity()));
		pairingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Activity parent = NotificationsFragment.this.getActivity();
				if (parent == null || !(parent instanceof MainActivity)) return;
				
				((MainActivity) parent).showPairingDialog(mPurifier);
				parent.onBackPressed();				
			}
		});
		
		TextView enableText = (TextView) rootView.findViewById(R.id.notifications_enable_all_text);
		String purifierName = (mPurifier == null ? "" : mPurifier.getName());
		enableText.setText(String.format(getString(R.string.notifications_enable_all), purifierName));
		
		notificationToggle =(ToggleButton) rootView.findViewById(R.id.notifications_enable_all_toggle);
		
		indoorAqiLbls= (LinearLayout) rootView.findViewById(R.id.notifications_indoor_aqi_lbls);
		indoorAqiRadioBtns= (RadioGroup) rootView.findViewById(R.id.notifications_indoor_radioGroup);				
	}
	
	private void setUIAqiThreshold(int aqiThreshold) {
		switch(aqiThreshold){
		case 13:
			RadioButton aqiRadioButton0= (RadioButton) indoorAqiRadioBtns.getChildAt(0);
			aqiRadioButton0.setChecked(true);
			break;
		case 19:
			RadioButton aqiRadioButton1= (RadioButton) indoorAqiRadioBtns.getChildAt(1);
			aqiRadioButton1.setChecked(true);
			break;
		case 29:
			RadioButton aqiRadioButton2= (RadioButton) indoorAqiRadioBtns.getChildAt(2);
			aqiRadioButton2.setChecked(true);
			break;
		case 40:
			RadioButton aqiRadioButton3= (RadioButton) indoorAqiRadioBtns.getChildAt(3);
			aqiRadioButton3.setChecked(true);
			break;
		default:
			break;
		}
	}

	private void showPairingLayout() {
		pairingLayout.setVisibility(View.VISIBLE);
		enableLayout.setVisibility(View.GONE);
		disableDetailedNotificationsLayout();
	}
	
	private void showNotificationsLayout(boolean enabled) {
		pairingLayout.setVisibility(View.GONE);
		enableLayout.setVisibility(View.VISIBLE);
		if (enabled) {
			enableDetailedNotificationsLayout();
		} else {
			disableDetailedNotificationsLayout();
		}
	}

	@SuppressLint("NewApi")
	private void enableDetailedNotificationsLayout() {
		notificationToggle.setChecked(true);
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
		notificationToggle.setChecked(false);
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
	
	private void notificationSetup() {
		if(!Utils.isGooglePlayServiceAvailable()){
			notificationToggle.setEnabled(false);
			return;
		}
		PurAirApplication.getAppContext().getNotificationRegisteringManager().registerAppForNotification();
		if (mPurifier != null && mPurifier.isPaired()) {
			
			showProgressDialog(R.string.notification_permission_check_msg);
			
			//Enable UI and check if permission exists
			pairingHandler.getPermission(AppConstants.PAIRING_NOTIFY_RELATIONSHIP, AppConstants.PAIRING_PUSH_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PUSH_PERMISSIONS.size()]));
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		switch(button.getId()){
		case R.id.notifications_enable_all_toggle:
			updateNotificationPermission(isChecked);
			break;
		default:
			break;
		}
	}
	
	private void updateNotificationPermission(boolean isChecked) {
		if(isChecked){
			showProgressDialog(R.string.notification_enabling_msg);
			enableDetailedNotificationsLayout();
			pairingHandler.addPermission(AppConstants.PAIRING_NOTIFY_RELATIONSHIP, AppConstants.PAIRING_PUSH_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PUSH_PERMISSIONS.size()]));
		}
		else{
			showProgressDialog(R.string.notification_disabling_msg);
			pairingHandler.removePermission(AppConstants.PAIRING_NOTIFY_RELATIONSHIP, AppConstants.PAIRING_PUSH_PERMISSIONS.toArray(new String[AppConstants.PAIRING_PUSH_PERMISSIONS.size()]));
			disableDetailedNotificationsLayout();
		}
	}
	private void setAQIThreshold(String aqiThreshold) {
		ALog.i(ALog.NOTIFICATION, "Setting AQIThreshold: "+aqiThreshold) ;
		if (mPurifier == null ) {
			return ;
		}
		showProgressDialog(R.string.notification_send_aqi_level_msg);
		PurifierManager.getInstance().setPurifierDetails(ParserConstants.AQI_THRESHOLD, aqiThreshold);
	}

	@Override
	public void onPermissionReturned(final boolean permissionExists) {
		if(progressDialog!=null)progressDialog.dismiss();
		
		ALog.i(ALog.NOTIFICATION, "Permission exists: "+permissionExists);
		// toggleOn the notification toggle	is permission Exists
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(permissionExists){
					enableDetailedNotificationsLayout();
				}
				else{
					disableDetailedNotificationsLayout();
				}
				notificationToggle.setOnCheckedChangeListener(NotificationsFragment.this);
				if(mPurifier!=null && mPurifier.getAirPortInfo() != null)
				{
					setUIAqiThreshold(mPurifier.getAirPortInfo().getAqiThreshold());
				}				
				indoorAqiRadioBtns.setOnCheckedChangeListener(NotificationsFragment.this);
			}
		});		
	}

	@Override
	public void onPermissionRemoved() {
		ALog.i(ALog.NOTIFICATION, "Permission removed");
		if (progressDialog!=null) progressDialog.dismiss();
		// toggleOff the notification toggle
	}

	@Override
	public void onPermissionAdded() {
		ALog.i(ALog.NOTIFICATION, "Permission added");
		// toggleOff the notification toggle
		if (progressDialog!=null) progressDialog.dismiss();
	}

	@Override
	public void onCallFailed() {
		ALog.i(ALog.NOTIFICATION, "Failed to change permissions");
		if (progressDialog!=null) progressDialog.dismiss();
		notificationSetup();
	}

	@Override
	public void onPositiveButtonClicked() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;
		
		((MainActivity) parent).onBackPressed();
		
	}

	@Override
	public void onNegativeButtonClicked() {
		// NOP
		
	}

	@Override
	public void onAirPurifierChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAirPurifierEventReceived() {
		ALog.i(ALog.NOTIFICATION, "aqi threshold added");
		if (progressDialog != null) progressDialog.dismiss();
	}

	@Override
	public void onFirmwareEventReceived() {
		// NOP
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.notifications_indoor_radio0:
			aqiThreshold="13";
			break;
		case R.id.notifications_indoor_radio1:
			aqiThreshold="19";
			break;
		case R.id.notifications_indoor_radio2:
			aqiThreshold="29";
			break;
		case R.id.notifications_indoor_radio3:
			aqiThreshold="40";
			break;
		default:
			break;
		}
		setAQIThreshold(aqiThreshold);
	}	
	
	private void showProgressDialog(int msg){
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(getString(msg));
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
}

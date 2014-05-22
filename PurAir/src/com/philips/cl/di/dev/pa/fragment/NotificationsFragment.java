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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.cpp.PairingManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Fonts;

public class NotificationsFragment extends BaseFragment implements OnCheckedChangeListener, PermissionListener, AirPurifierEventListener, android.widget.RadioGroup.OnCheckedChangeListener, AlertDialogBtnInterface {

	private RelativeLayout pairingLayout;
	private RelativeLayout enableLayout;
	private LinearLayout detailedLayout;
	
	private Button pairingButton;
	private ToggleButton notificationToggle;
	private LinearLayout indoorAqiLbls;
	private RadioGroup indoorAqiRadioBtns;
	
	private PairingManager pairingManager;
	private ProgressDialog progressDialog;
	private AirPurifierController airPurifierController ;
	
	private PurAirDevice mPurifier;

	private String aqiThreshold ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPurifier = ((MainActivity) getActivity()).getCurrentPurifier();
		pairingManager = new PairingManager(null, mPurifier);
		pairingManager.setPermissionListener(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notifications_fragment, container, false);
		initializeAllViews(view);
		airPurifierController = AirPurifierController.getInstance() ;
		return view;
	}
	
	@Override
	public void onResume() {
		if (mPurifier == null) {
			Activity parent = this.getActivity();
			if (parent != null && parent instanceof MainActivity) {
				AlertDialogFragment dialog = AlertDialogFragment.newInstance(R.string.notification_nopurifier_title, R.string.notification_nopurifier_text, R.string.notification_nopurifier_positivebtn);
				dialog.setOnClickListener(this);
				dialog.show(((MainActivity) parent).getSupportFragmentManager(), null);
			}
		}
		
		
		boolean notificationsEnabled = true; // TODO replace by actual pairing check
		if (mPurifier == null) {
			showNotificationsLayout(false);
		} else if (mPurifier.isPaired()) {
			notificationSetup();
			showNotificationsLayout(notificationsEnabled);
		} else {
			showPairingLayout();
		}
		super.onResume();
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
		notificationToggle.setOnCheckedChangeListener(this);
		
		indoorAqiLbls= (LinearLayout) rootView.findViewById(R.id.notifications_indoor_aqi_lbls);
		indoorAqiRadioBtns= (RadioGroup) rootView.findViewById(R.id.notifications_indoor_radioGroup);
		indoorAqiRadioBtns.setOnCheckedChangeListener(this);
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
//		notificationToggle.setChecked(true);
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
	
	private void notificationSetup(){
		if(mPurifier!=null && mPurifier.isPaired()) {
			
			/*progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getString(R.string.pairing_progress));
			progressDialog.setCancelable(false);
			progressDialog.show();*/

			
			//Enable UI and check if permission exists
			pairingManager.getPermission(AppConstants.NOTIFY_RELATIONSHIP, AppConstants.PUSH_PERMISSIONS.toArray(new String[AppConstants.PUSH_PERMISSIONS.size()]));
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
	
	private void updateNotificationPermission(boolean isChecked)
	{
		if(isChecked){
			enableDetailedNotificationsLayout();
			pairingManager.addPermission(AppConstants.NOTIFY_RELATIONSHIP, AppConstants.PUSH_PERMISSIONS.toArray(new String[AppConstants.PUSH_PERMISSIONS.size()]));
		}
		else{
			pairingManager.removePermission(AppConstants.NOTIFY_RELATIONSHIP, AppConstants.PUSH_PERMISSIONS.toArray(new String[AppConstants.PUSH_PERMISSIONS.size()]));
			disableDetailedNotificationsLayout();
		}
	}
	private void setAQIThreshold(String aqiThreshold) {
		ALog.i(ALog.NOTIFICATION, "Setting AQIThreshold: "+aqiThreshold) ;
		PurifierManager.getInstance().addAirPurifierEventListener(this) ;
		if (mPurifier == null ) {
			return ;
		}
		// TODO - Progress Dialog
		switch (mPurifier.getConnectionState()) {
			case CONNECTED_LOCALLY 	: airPurifierController.setDeviceDetailsLocally(ParserConstants.AQI_THRESHOLD, aqiThreshold, mPurifier);
			case CONNECTED_REMOTELY : airPurifierController.setDeviceDetailsRemotely(ParserConstants.AQI_THRESHOLD, aqiThreshold, mPurifier);
			case DISCONNECTED		: //NOP
		}
	}

	@Override
	public void onPermissionReturned(final boolean permissionExists) {
		ALog.i(ALog.NOTIFICATION, "Permission exists: "+permissionExists);
		// toggleOn the notification toggle	is permission Exists
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notificationToggle.setChecked(permissionExists);
			}
		});		
	}

	@Override
	public void onPermissionRemoved() {
		ALog.i(ALog.NOTIFICATION, "Permission removed");
		// toggleOff the notification toggle
	}

	@Override
	public void onPermissionAdded() {
		ALog.i(ALog.NOTIFICATION, "Permission added");
		// toggleOff the notification toggle
	}

	@Override
	public void onCallFailed() {
		// TODO implement
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
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		//TODO - Stop the progress dialog
	}

	@Override
	public void onFirmwareEventReceived() {
		// TODO Auto-generated method stub
		
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
}

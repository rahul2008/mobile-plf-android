package com.philips.cl.di.dev.pa.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Activity;
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
import com.philips.cl.di.dev.pa.util.Fonts;

public class NotificationsFragment extends BaseFragment implements OnCheckedChangeListener, OnClickListener, PermissionListener, AirPurifierEventListener {

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
		boolean notificationsEnabled = true; // TODO replace by actual pairing check
		if (mPurifier.isPaired()) {
			showNotificationsLayout(notificationsEnabled);
		} else {
			showPairingLayout();
		}
		notificationSetup();
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
		
		pairingButton = (Button) rootView.findViewById(R.id.btn_notifications_pairing) ;
		pairingButton.setOnClickListener(this) ;

		indoorAqiLbls= (LinearLayout) rootView.findViewById(R.id.notifications_indoor_aqi_lbls);
		indoorAqiRadioBtns= (RadioGroup) rootView.findViewById(R.id.notifications_indoor_radioGroup);
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
	
	private void notificationSetup(){
		if(mPurifier!=null && mPurifier.isPaired()) {
			//Enable UI and check if permission exists
			pairingManager=new PairingManager(null, mPurifier);
			pairingManager.setPermissionListener(this);
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
	public void onClick(View v) {
		ALog.i(ALog.NOTIFICATION, "OnClick: "+v.getId()) ;
		aqiThreshold = "30" ;
		setAQIThreshold(aqiThreshold) ;
		switch(v.getId()){			
		default:
			break;
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
		
	}

	@Override
	public void onAirPurifierChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAirPurifierEventReceived() {
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		//TODO - Stop the progress dialog
	}

	@Override
	public void onFirmwareEventReceived() {
		// TODO Auto-generated method stub
		
	}	
}

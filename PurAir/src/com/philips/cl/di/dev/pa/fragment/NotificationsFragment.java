package com.philips.cl.di.dev.pa.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.PairingManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;

public class NotificationsFragment extends BaseFragment implements OnCheckedChangeListener, OnClickListener, PermissionListener{
	
	private LinearLayout indoorAqiLbls;
	private RadioGroup indoorAqiRadioBtns;
	
	private TextView indoorTitle;
	private TextView indoorDescription;
	private TextView filtersTitle;
	private TextView filtersDescription;
	
	private ToggleButton notificationToggle;
	private PairingManager pairingManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notifications_fragment, container, false);
		initializeView(view);
		return view;
	}

	private void initializeView(View rootView) {
		notificationToggle =(ToggleButton) rootView.findViewById(R.id.toggle_notifications_enable_all);
		notificationToggle.setOnCheckedChangeListener(this);
		
		indoorTitle = (TextView) rootView.findViewById(R.id.notifications_indoor_title);
		indoorDescription = (TextView) rootView.findViewById(R.id.notifications_indoor_description);
		indoorAqiLbls= (LinearLayout) rootView.findViewById(R.id.notifications_indoor_aqi_lbls);
		indoorAqiRadioBtns= (RadioGroup) rootView.findViewById(R.id.notifications_indoor_radioGroup);
		
		filtersTitle = (TextView) rootView.findViewById(R.id.notifications_filters_title);
		filtersDescription = (TextView) rootView.findViewById(R.id.notifications_filters_description);
		
		notificationSetup();
	}
	
	private void notificationSetup(){
		PurAirDevice purifier= PurifierManager.getInstance().getCurrentPurifier();
		if(purifier.isPaired())
		{
			//Enable UI and check if permission exists
			pairingManager=new PairingManager(null, purifier);
			pairingManager.setPermissionListener(this);
			pairingManager.getPermission(AppConstants.NOTIFY_RELATIONSHIP, AppConstants.PUSH_PERMISSIONS.toArray(new String[AppConstants.PUSH_PERMISSIONS.size()]));
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		switch(button.getId()){
		case R.id.toggle_notifications_enable_all:
			updateNotificationPermission(isChecked);
			break;
		default:
			break;
		}
	}
	
	private void updateNotificationPermission(boolean isChecked)
	{
		if(isChecked){
			indoorAqiLbls.setVisibility(View.VISIBLE);
			indoorAqiRadioBtns.setVisibility(View.VISIBLE);
//			indoorTitle.setAlpha(1);
//			indoorDescription.setAlpha(1);
//			filtersTitle.setAlpha(1);
//			filtersDescription.setAlpha(1);
			pairingManager.addPermission(AppConstants.NOTIFY_RELATIONSHIP, AppConstants.PUSH_PERMISSIONS.toArray(new String[AppConstants.PUSH_PERMISSIONS.size()]));
		}
		else{
			pairingManager.removePermission(AppConstants.NOTIFY_RELATIONSHIP, AppConstants.PUSH_PERMISSIONS.toArray(new String[AppConstants.PUSH_PERMISSIONS.size()]));
			indoorAqiLbls.setVisibility(View.GONE);
			indoorAqiRadioBtns.setVisibility(View.GONE);
//			indoorTitle.setAlpha(0.5f);
//			indoorDescription.setAlpha(0.5f);
//			filtersTitle.setAlpha(0.5f);
//			filtersDescription.setAlpha(0.5f);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){			
		default:
			break;
		}
	}

	@Override
	public void onPermissionReturned(boolean permissionExists) {
		ALog.i(ALog.NOTIFICATION, "Permission exists");
		// toggleOn the notification toggle	is permission Exists
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
}

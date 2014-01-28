package com.philips.cl.di.dev.pa.pureairui.fragments;

import com.philips.cl.di.dev.pa.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class NotificationsFragment extends Fragment implements OnCheckedChangeListener, OnClickListener {
	Button dailyNotificationButtonEdit;
	Button dailyNotificationButtonAdd;
	ScrollView notificationListLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notifications_fragment, container, false);
		initializeView(view);
		return view;
	}

	private void initializeView(View rootView) {
		ToggleButton dailyNotificationToggle=(ToggleButton) rootView.findViewById(R.id.daily_notification_toggle);
		ToggleButton outdoorNotificationToggle=(ToggleButton) rootView.findViewById(R.id.outdoor_AQI_notification_toggle);
		ToggleButton indoorNotificationToggle =(ToggleButton) rootView.findViewById(R.id.indoor_aqi_notification_toggle);
		ToggleButton filterNotificationToggle=(ToggleButton) rootView.findViewById(R.id.filters_notification_toggle);
		
		dailyNotificationToggle.setOnCheckedChangeListener(this);
		outdoorNotificationToggle.setOnCheckedChangeListener(this);
		indoorNotificationToggle.setOnCheckedChangeListener(this);
		filterNotificationToggle.setOnCheckedChangeListener(this);
		
		dailyNotificationButtonEdit= (Button) rootView.findViewById(R.id.daily_notification_btn_edit);
		dailyNotificationButtonAdd=(Button) rootView.findViewById(R.id.daily_notification_btn_add);
		notificationListLayout=(ScrollView) rootView.findViewById(R.id.scrollview_notification_layout);
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		switch(button.getId()){
		case R.id.daily_notification_toggle:
			if(isChecked){
				showDailyNotificationUI();
			}
			break;
		case R.id.outdoor_AQI_notification_toggle:
			break;
		case R.id.indoor_aqi_notification_toggle:
			break;
		case R.id.filters_notification_toggle:
			break;
		}
	}

	private void showDailyNotificationUI() {
		dailyNotificationButtonEdit.setVisibility(View.VISIBLE);
		dailyNotificationButtonAdd.setVisibility(View.VISIBLE);
		notificationListLayout.setVisibility(View.VISIBLE);
		dailyNotificationButtonAdd.setOnClickListener(this);
		dailyNotificationButtonEdit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.daily_notification_btn_add:
			break;
		case R.id.daily_notification_btn_edit:
			break;
		}
	}
}

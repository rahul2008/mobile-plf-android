package com.philips.cl.di.dev.pa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.CreateNotificationActivity;

public class NotificationsFragment extends BaseFragment implements OnCheckedChangeListener, OnClickListener {
	private static final int CREATE_NOTIFICATION = 2;
	private Button dailyNotificationButtonEdit;
	private Button dailyNotificationButtonAdd;
	private ScrollView notificationListLayout;
	private LinearLayout notificationLayout;
	private View separator;
	private ImageButton deleteImgBtn;
	private Button deleteBtn;
	private ToggleButton notificationToggle;
	private View inflatedLayout;
	private RadioGroup indoorAqiLevels,outdoorAqiLevels;
	private LinearLayout indoorAqiLbls;
	private LinearLayout outdoorAqiLbls;

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
		notificationLayout=(LinearLayout)rootView.findViewById(R.id.notification_layout);
		separator=rootView.findViewById(R.id.daily_notification_separator);
		indoorAqiLevels=(RadioGroup) rootView.findViewById(R.id.indoor_radioGroup);
		outdoorAqiLevels=(RadioGroup) rootView.findViewById(R.id.outdoor_radioGroup);
		indoorAqiLbls=(LinearLayout)rootView.findViewById(R.id.indoor_aqi_lbls);
		outdoorAqiLbls=(LinearLayout)rootView.findViewById(R.id.outdoor_aqi_lbls);
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		switch(button.getId()){
		case R.id.daily_notification_toggle:
			showDailyNotificationUI(isChecked);
			break;
		case R.id.outdoor_AQI_notification_toggle:
			showOutdoorAqilLevels(isChecked);
			break;
		case R.id.indoor_aqi_notification_toggle:
			showIndoorAqilLevels(isChecked);
			break;
		case R.id.filters_notification_toggle:
			break;
		default:
			break;
		}
	}

	private void showDailyNotificationUI(boolean isChecked) {
		if(isChecked){
			dailyNotificationButtonEdit.setVisibility(View.VISIBLE);
			dailyNotificationButtonAdd.setVisibility(View.VISIBLE);
			notificationListLayout.setVisibility(View.VISIBLE);
			dailyNotificationButtonAdd.setOnClickListener(this);
			dailyNotificationButtonEdit.setOnClickListener(this);
		}
		else{
			dailyNotificationButtonEdit.setVisibility(View.GONE);
			dailyNotificationButtonAdd.setVisibility(View.GONE);
			notificationListLayout.setVisibility(View.GONE);
			separator.setVisibility(View.VISIBLE);
		}
	}
	
	private void showIndoorAqilLevels(boolean isChecked)
	{
		if(isChecked){
			indoorAqiLevels.setVisibility(View.VISIBLE);
			indoorAqiLbls.setVisibility(View.VISIBLE);
		}
		else{
			indoorAqiLevels.setVisibility(View.GONE);
			indoorAqiLbls.setVisibility(View.GONE);
		}
	}
	
	private void showOutdoorAqilLevels(boolean isChecked)
	{
		if(isChecked){
			outdoorAqiLevels.setVisibility(View.VISIBLE);
			outdoorAqiLbls.setVisibility(View.VISIBLE);
		}
		else{
			outdoorAqiLevels.setVisibility(View.GONE);
			outdoorAqiLbls.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){		
		case R.id.daily_notification_btn_add:
			if(inflatedLayout!=null){
				deleteImgBtn.setVisibility(View.GONE);
				deleteBtn.setVisibility(View.GONE);
				notificationToggle.setVisibility(View.VISIBLE);
			}
			Intent in=new Intent(getActivity(), CreateNotificationActivity.class);
			startActivityForResult(in, CREATE_NOTIFICATION);
			break;
		case R.id.daily_notification_btn_edit:
			if(inflatedLayout!=null){
				deleteImgBtn.setVisibility(View.VISIBLE);
				deleteBtn.setVisibility(View.VISIBLE);
				notificationToggle.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);		
		if(resultCode==Activity.RESULT_OK && requestCode==CREATE_NOTIFICATION){
			String time= data.getStringExtra("SELECTED_TIME_STRING");
			String days=data.getStringExtra("SELECTED_DAYS_STRING");		
			updateUI(time, days);
		}
	}

	private void updateUI(String time, String days) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		inflatedLayout= inflater.inflate(R.layout.notification_list_item_view, null, false);
		notificationListLayout.setPadding(20, 20, 20, 20);
		notificationLayout.addView(inflatedLayout);
		TextView selectedDaysLbl=(TextView) inflatedLayout.findViewById(R.id.notification_selected_days);
		TextView selectedTimeLbl=(TextView) inflatedLayout.findViewById(R.id.notification_selected_time);
		notificationToggle=(ToggleButton) inflatedLayout.findViewById(R.id.independent_notification_toggle);
		deleteImgBtn=(ImageButton) inflatedLayout.findViewById(R.id.deleteImageButton);
		deleteBtn=(Button) inflatedLayout.findViewById(R.id.btn_delete);

		selectedDaysLbl.setText(days);
		selectedTimeLbl.setText(time);
		separator.setVisibility(View.GONE);
	}
}

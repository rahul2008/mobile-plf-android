package com.philips.cl.di.dev.pa.activity;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;

public class CreateNotificationActivity extends BaseActivity implements OnClickListener{

	private static final int SELECT_DAY_ACTIVITY = 10;
	private String strDays="";
	TimePicker mTimePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_notifications);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.MAINACTIVITY, "Actionbar: " + e.getMessage());
		}
		setActionBarTitle(R.string.set_notification);
		
		RelativeLayout addNotificationLayout= (RelativeLayout) findViewById(R.id.select_days_layout);
		addNotificationLayout.setOnClickListener(this);
		mTimePicker=(TimePicker) findViewById(R.id.notification_timePicker);
		mTimePicker.setIs24HourView(true);
		setCurrentTimeOnView();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.select_days_layout:
			Intent in= new Intent(this, NotificationSelectDayActivity.class);
			startActivityForResult(in, SELECT_DAY_ACTIVITY);
			break;
		default:
			break;
		}
	}

	// display current time
	public void setCurrentTimeOnView() {

		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// set current time into time picker
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(minute);

	}

	private static String paddingStr(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==SELECT_DAY_ACTIVITY)
		{
			String[] arrDays=data.getStringArrayExtra("SELECTED_DAYS");			
			for(int i=0; i<arrDays.length;i++)
			{
				strDays=strDays+arrDays[i];
				if(i<arrDays.length-1)
					strDays= strDays +", ";
			}
			TextView selectedDaysLbl= (TextView) findViewById(R.id.selected_days_lbl);
			selectedDaysLbl.setTypeface(Fonts.getGillsans(this));
			selectedDaysLbl.setText(strDays);
		}
	}
	
	@Override
	public void onBackPressed() {
		returnSelectedDays();
	}
	
	public void returnSelectedDays()
	{
		String selectedTime= new StringBuilder().append(paddingStr(mTimePicker.getCurrentHour())).append(":").append(paddingStr(mTimePicker.getCurrentMinute())).toString();
		        
        Intent in= new Intent();
        in.putExtra("SELECTED_DAYS_STRING", strDays);
        in.putExtra("SELECTED_TIME_STRING", selectedTime);
        setResult(RESULT_OK,in);
        finish();
	}
	
	/*Initialize action bar */
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = getLayoutInflater().inflate(R.layout.home_action_bar, null);
		((ImageView)view.findViewById(R.id.right_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.left_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.back_to_home_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.add_location_img)).setVisibility(View.GONE);
		actionBar.setCustomView(view);		
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int tutorialTitle) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(this));
		textView.setTextSize(24);
		textView.setText(this.getText(tutorialTitle));
	}
}

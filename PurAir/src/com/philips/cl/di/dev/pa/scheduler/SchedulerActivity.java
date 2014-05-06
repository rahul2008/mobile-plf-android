package com.philips.cl.di.dev.pa.scheduler;

import java.util.List;
import com.philips.cl.di.dev.pa.R;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerActivity extends BaseActivity implements OnClickListener,
		ServerResponseListener, OnTimeSetListener {

	private static boolean cancelled;
	private Button actionBarCancelBtn;
	private Button actionBarBackBtn;
	private FontTextView actionbarTitle;
	private String SelectedDays;
	private String SelectedTime;
	private String SelectedFanspeed;
	private JsonArray arrSchedulers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.i("Scheduler", "onCreate method beginning");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_container);
		initActionBar();		
		Intent intent = getIntent();
		arrSchedulers = new JsonArray();
		JsonArray arr = getSchedulerDataFromProduct();
		Bundle b = new Bundle();
		b.putString("events", arr.toString());
		intent.putExtras(b);
		showFragment();
		ALog.i("Scheduler", "onCreate method ending");
	}

	private void showFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();
	}

	private JsonArray getSchedulerDataFromProduct() {
		ALog.i("Scheduler", "getSchedulerDataFromProduct method beginning");
		// TODO: To be replaced with actual code gets JSON values from the
		
		try {
			JsonObject jo = new JsonObject();
			jo.addProperty("enabled", "true");
			jo.addProperty("time", "06:15");
			jo.addProperty("days", "0123456");
			jo.addProperty("product", "2");
			jo.addProperty("port", "air");
			jo.addProperty("speed", "Silent");
			jo.addProperty("command", "{“om” : “a”}");
			arrSchedulers.add(jo);
		} catch (Exception e) {
			ALog.i("Scheduler", "exception in getSchedulerDataFromProduct method");
		}

		ALog.i("Scheduler", "getSchedulerDataFromProduct method ending");
		return arrSchedulers;
	}
	
	private void initActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

		Drawable d = getResources().getDrawable(R.drawable.ews_nav_bar_2x);
		actionBar.setBackgroundDrawable(d);

		View view = getLayoutInflater().inflate(R.layout.scheduler_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.scheduler_actionbar_title);
		actionbarTitle.setText(getString(R.string.scheduler));

		actionBarCancelBtn = (Button) view.findViewById(R.id.scheduler_actionbar_add_btn);
		actionBarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionBarCancelBtn.setOnClickListener(this);
		actionBarCancelBtn.setOnClickListener(onClickListener);

		actionBarBackBtn = (Button) view.findViewById(R.id.larrow);
		actionBarBackBtn.setOnClickListener(onClickListener);

		actionBar.setCustomView(view);
	}
	
	private void SaveScheduler() {
		ALog.i("Scheduler", "SaveScheduler method beginning");
		JsonObject jo = new JsonObject();
		jo.addProperty("enabled", "true");
		jo.addProperty("time", SelectedTime);
		jo.addProperty("days", SelectedDays);
		jo.addProperty("product", "2");
		jo.addProperty("port", "air");
		jo.addProperty("speed", SelectedFanspeed);
		jo.addProperty("command", "{“om” : “a”}");
		arrSchedulers.add(jo);

		Bundle b = new Bundle();
		b.putString("events", arrSchedulers.toString());
		getIntent().putExtras(b);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();
		ALog.i("Scheduler", "SaveScheduler method ending");
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			ALog.i("Scheduler", "onClick method beginning called");
			
			switch (v.getId()) {
			case R.id.scheduler_actionbar_add_btn:
				ALog.i("Scheduler", "onClick method - case scheduler_actionbar_add_btn is called");
				SaveScheduler();
				break;
				
			case R.id.larrow:
				ALog.i("Scheduler", "onClick method - case larrow is called");
				Bundle bundle = new Bundle();
				bundle.putString("time", SelectedTime);
				bundle.putString("days", SelectedDays);
				bundle.putString("fanspeed", SelectedFanspeed);

				AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
				fragAddSch.setArguments(bundle);

				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment").commit();

				ALog.i("Scheduler", "onClick method ending called");
				break;
			}
		}
	};

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
		String time;
		String name;
		time = String.format("%d:%02d", hourOfDay, minute);
		// String time = hourOfDay + ":" + minute;
		SelectedTime = time;

		List<android.support.v4.app.Fragment> lstFragment = getSupportFragmentManager().getFragments();
		
		for (android.support.v4.app.Fragment frg : lstFragment) {
			name = frg.getClass().getSimpleName();
			if (frg != null) {
				ALog.i("Scheduler", "To know fragment inside onTimeSet method "	+ frg.getClass().getSimpleName());
			}
		}

		Bundle bundle = new Bundle();
		bundle.putString("time", time);
		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);

		actionbarTitle.setText("Add Event");

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragAddSch,
						"AddSchedulerFragment").commit();

		ALog.i("Scheduler",	"Scheduler Activity - onTimeSet method is completed");
	}

	public void setActionBar(SchedulerID id) {
		switch (id) {
		case OVERVIEW_EVENT:
			setActionBar("Scheduler", View.INVISIBLE, View.INVISIBLE);
			break;
		case ADD_EVENT:
			setActionBar("Set Schedule", View.VISIBLE, View.VISIBLE);
			break;
		case REPEAT:
			ALog.i("Scheduler", "Repeat is called");
			setActionBar("Repeat", View.INVISIBLE, View.VISIBLE);
			break;
		case FAN_SPEED:
			ALog.i("Scheduler", "Repeat is called");
			setActionBar("Fan speed", View.INVISIBLE, View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void setActionBar(String textId, int cancelButton, int backButton) {
		actionbarTitle.setText(textId);
		actionBarCancelBtn.setVisibility(cancelButton);
		actionBarBackBtn.setVisibility(backButton);
		// actionBarBackBtn.setVisibility(View.INVISIBLE);
		ALog.i("Scheduler", "SetBackBtn is called");
	}


	public void dispatchInformations(String days) {
		ALog.i("Scheduler", "dispaychInformations - days is " + days);
		SelectedDays = days;
	}

	public void dispatchInformations2(String fanspeed) {
		ALog.i("Scheduler", "dispatchInformation2 - fanspeed is " + fanspeed);
		SelectedFanspeed = fanspeed;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		setCancelled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setCancelled(false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	@Override
	public void onClick(View v) {
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
	}	
		
	public static void setCancelled(boolean cancelled) {
	}

	public static boolean isCancelled() {
		return cancelled;
	}
}

package com.philips.cl.di.dev.pa.scheduler;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;import com.philips.cl.di.dev.pa.R;
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
	private String updateSelectedDays;
	private String updateSelectedTime;
	private String updateSelectedFanspeed;
	private String CRUDOperation = "";
	private int UDOperationIndex = -1;
	private JSONArray arrSchedulers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::onCreate() method enter");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_container);
		initActionBar();
		arrSchedulers = new JSONArray();
		/*Intent intent = getIntent();
		JSONArray arr = getSchedulerDataFromProduct();
		Bundle b = new Bundle();
		b.putString("events", arr.toString());
		intent.putExtras(b);*/
		showSchedulerOverviewFragment();
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::onCreate() method exit");
	}

	private void showSchedulerOverviewFragment() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::showSchedulerOverviewFragment() method enter");
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::showSchedulerOverviewFragment() method exit");
	}
	
	private void showAddSchedulerFragment(Bundle bundle) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::showAddSchedulerFragment() method enter");
		AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment").commit();

		
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::showAddSchedulerFragment() method exit");
	}
	
	public JSONArray getSchedulerList() {
		return arrSchedulers;
	}

	private JSONArray getSchedulerDataFromProduct() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::getSchedulerDataFromProduct() method enter");
		// TODO: To be replaced with actual data from Purifier
		
		try {
			JSONObject jo = new JSONObject();
			jo.put(SchedulerConstants.ENABLED, "true");
			jo.put(SchedulerConstants.TIME, "06:15");
			jo.put(SchedulerConstants.DAYS, "0123456");
			jo.put(SchedulerConstants.PRODUCT, "2");
			jo.put(SchedulerConstants.PORT, "air");
			jo.put(SchedulerConstants.SPEED, "Silent");
			jo.put(SchedulerConstants.COMMAND, "om-a");
			arrSchedulers.put(jo);
		} catch (Exception e) {
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::getSchedulerDataFromProduct() method exception caught while adding object to Schedulers list");
		}

		ALog.i(ALog.SCHEDULER, "SchedulerActivity::getSchedulerDataFromProduct() method exit");
		return arrSchedulers;
	}
	
	private void initActionBar() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::initActionBar() method enter");
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
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::initActionBar() method exit");
	}
	
	private void Save() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::Save() method enter");
		
		if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			createScheduler();
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			updateScheduler();
		} else if (CRUDOperation.isEmpty()) {
			/*getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();*/
			showSchedulerOverviewFragment();
		}
		
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::Save() method exit");
	}
	
	private void createScheduler() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::createScheduler() method enter");
		try{
				JSONObject jo = new JSONObject();
				jo.put(SchedulerConstants.ENABLED, "true");
				jo.put(SchedulerConstants.TIME, SelectedTime);
				jo.put(SchedulerConstants.DAYS, SelectedDays);
				jo.put(SchedulerConstants.PRODUCT, "2");
				jo.put(SchedulerConstants.PORT, "air");
				jo.put(SchedulerConstants.SPEED, SelectedFanspeed);
				jo.put(SchedulerConstants.COMMAND, "{“om” : “a”}");
				arrSchedulers.put(jo);
				} catch(Exception e) {
		}
			
		Bundle b = new Bundle();
		b.putString("events", arrSchedulers.toString());
		getIntent().putExtras(b);

		/*getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();*/
		showSchedulerOverviewFragment();
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::createScheduler() method exit");
	}
	
	private void updateScheduler() {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::updateScheduler() method enter");
		try {
			JSONObject jo = arrSchedulers.getJSONObject(UDOperationIndex);
			jo.put(SchedulerConstants.TIME, updateSelectedTime);
			jo.put(SchedulerConstants.DAYS, updateSelectedDays);
			jo.put(SchedulerConstants.SPEED, updateSelectedFanspeed);
			
			Bundle b = new Bundle();
			b.putString("events", arrSchedulers.toString());
			getIntent().putExtras(b);
	
			/*getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.ll_scheduler_container, new SchedulerOverviewFragment(), "SchedulerOverviewFragment").commit();*/
			showSchedulerOverviewFragment();
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::updateScheduler() method exit");
		}
		catch(Exception e) {
		}
	}
	
	private void deleteScheduler() {
		
	}
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::onClick() method enter");
			
			switch (v.getId()) {
			case R.id.scheduler_actionbar_add_btn:
				ALog.i(ALog.SCHEDULER, "onClick method - case scheduler_actionbar_add_btn is called");
				Save();
				break;
				
			case R.id.larrow:
				ALog.i(ALog.SCHEDULER, "onClick method - case larrow is called");
				Bundle bundle = new Bundle();
				if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
					bundle.putString(SchedulerConstants.TIME, SelectedTime);
					bundle.putString(SchedulerConstants.DAYS, SelectedDays);
					bundle.putString(SchedulerConstants.SPEED, SelectedFanspeed);
				} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
					bundle.putString(SchedulerConstants.TIME, updateSelectedTime);
					bundle.putString(SchedulerConstants.DAYS, updateSelectedDays);
					bundle.putString(SchedulerConstants.SPEED, updateSelectedFanspeed);
				}

				/*AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
				fragAddSch.setArguments(bundle);

				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.ll_scheduler_container, fragAddSch, "AddSchedulerFragment").commit();*/
				showAddSchedulerFragment(bundle);

				ALog.i(ALog.SCHEDULER, "onClick method ending called");
				break;
			}
			ALog.i(ALog.SCHEDULER, "SchedulerActivity::onClick() method exit");
		}
	};

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method enter");
		String time;
		time = String.format("%d:%02d", hourOfDay, minute);
		// String time = hourOfDay + ":" + minute;
		//SelectedTime = time;
		Bundle bundle = new Bundle();
		//bundle.putString("time", time);
		
		if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			SelectedTime = time;
			bundle.putString(SchedulerConstants.TIME, SelectedTime);
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			updateSelectedTime = time;
			bundle.putString(SchedulerConstants.TIME, updateSelectedTime);
			bundle.putString(SchedulerConstants.DAYS, updateSelectedDays);
			bundle.putString(SchedulerConstants.SPEED, updateSelectedFanspeed);
		}
		
		actionbarTitle.setText("Add Event");
		
		/*AddSchedulerFragment fragAddSch = new AddSchedulerFragment();
		fragAddSch.setArguments(bundle);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.ll_scheduler_container, fragAddSch,
						"AddSchedulerFragment").commit();*/
		showAddSchedulerFragment(bundle);

		ALog.i(ALog.SCHEDULER, "SchedulerActivity::onTimeSet() method exit");
	}

	public void setActionBar(SchedulerID id) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::setActionBar() method enter");
		switch (id) {
		case OVERVIEW_EVENT:
			setActionBar(R.string.scheduler, View.INVISIBLE, View.INVISIBLE);
			break;
		case ADD_EVENT:
			if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
				setActionBar(R.string.set_schedule, View.VISIBLE, View.VISIBLE);
			} else {
				setActionBar(R.string.edit_schedule, View.VISIBLE, View.VISIBLE);
			}
			break;
		case REPEAT:
			setActionBar(R.string.repeat, View.INVISIBLE, View.VISIBLE);
			break;
		case FAN_SPEED:
			setActionBar(R.string.fanspeed, View.INVISIBLE, View.VISIBLE);
			break;
		default:
			break;
		}
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::setActionBar() method exit");
	}

	private void setActionBar(int textId, int cancelButton, int backButton) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::setActionBar() method enter");
		actionbarTitle.setText(textId);
		actionBarCancelBtn.setVisibility(cancelButton);
		actionBarBackBtn.setVisibility(backButton);
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::setActionBar() method exit");
	}


	public void dispatchInformations(String days) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::dispatchInformations() method - days is " + days);
		/*if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			SelectedDays = days;
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			updateSelectedDays = days;
		}*/
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, days, SchedulerConstants.EMPTY_STRING);
	}

	public void dispatchInformations2(String fanspeed) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::dispatchInformations2() method - fanspeed is " + fanspeed);
		/*if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			SelectedFanspeed = fanspeed;
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			updateSelectedFanspeed = fanspeed;
		}*/
		updateCRUDOperationData(SchedulerConstants.EMPTY_STRING, SchedulerConstants.EMPTY_STRING, fanspeed);
	}
	
	public void dispatchInformationsForCRUD(String crud) {
		ALog.i(ALog.SCHEDULER, "SchedulerActivity::dispatchInformationsForCRUD() method - crud is " + crud);
		CRUDOperation = crud;
	}
	
	public void dispatchInformationsForCRUDIndex(int index) {
		ALog.i(ALog.SCHEDULER, "dispatchInformationsForCRUDIndex - index is " + index);
		UDOperationIndex = index;
		try {
			JSONObject jo = arrSchedulers.getJSONObject(UDOperationIndex);
			updateSelectedTime = jo.getString(SchedulerConstants.TIME);
			updateSelectedDays = jo.getString(SchedulerConstants.DAYS);
			updateSelectedFanspeed = jo.getString(SchedulerConstants.SPEED);
		} catch (Exception e) {
		}
	}
	
	private void updateCRUDOperationData(String time, String date, String speed) {
		if (CRUDOperation.equals(SchedulerConstants.CREATE_EVENT)) {
			if (!time.isEmpty())
				SelectedTime = time;
			if (!date.isEmpty())
				SelectedDays = date;
			if (!speed.isEmpty())
				SelectedFanspeed = speed;
		} else if (CRUDOperation.equals(SchedulerConstants.UPDATE_EVENT)) {
			if (!time.isEmpty())
				updateSelectedTime = time;
			if (!date.isEmpty())
				updateSelectedDays = date;
			if (!speed.isEmpty())
				updateSelectedFanspeed = speed;
		}
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

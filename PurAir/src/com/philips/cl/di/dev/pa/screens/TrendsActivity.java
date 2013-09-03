package com.philips.cl.di.dev.pa.screens;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.screens.adapters.GraphAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.CustomGraphView.GraphViewData;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.utils.Utils;

public class TrendsActivity extends FragmentActivity implements
		OnClickListener, OnFocusChangeListener {

	private RelativeLayout rlDay, rlWeek, rlMonth;
	// private CustomGraphView graph;

	private List<GraphViewData> listGraphViewDataIndoor;
	private List<GraphViewData> listGraphViewDataOutdoor;
	private List<Integer> listAQIIndoor;
	private List<Integer> listAQIOutdoor;
	private ImageView ivLeftMenu, ivRightDevice;
	private CustomTextView tvDayLabel, tvHomePercent, tvAVGPMValue, tvOnValue,
			tvCity, tvAVGPMValueOutdoor, tvOutdoorPercent, tvCityLabel;
	private String sCityName;
	private DatabaseAdapter dbAdapter;
	private String sDay;
	private ViewPager pagerGraph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trends);
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();

		Bundle b = getIntent().getExtras();
		sCityName = b.getString(AppConstants.CITYNAME);
		sDay = Utils.getCurrentDateForDB();
		listAQIIndoor = dbAdapter.getAQIValuesForDay(sDay,
				AppConstants.CITY_ID_HOME);
		listAQIOutdoor = dbAdapter.getAQIValuesForDay(sDay,
				Utils.getCityIDFromName(sCityName));
		initialiseViews();
		updateUI();
	}

	private void initialiseViews() {
		rlDay = (RelativeLayout) findViewById(R.id.rlDay);
		rlWeek = (RelativeLayout) findViewById(R.id.rlWeek);
		rlMonth = (RelativeLayout) findViewById(R.id.rlMonth);
		rlDay.setOnClickListener(this);
		rlDay.setOnFocusChangeListener(this);
		rlWeek.setOnClickListener(this);
		rlWeek.setOnFocusChangeListener(this);
		rlMonth.setOnClickListener(this);
		rlMonth.setOnFocusChangeListener(this);

		pagerGraph = (ViewPager) findViewById(R.id.pagerGraph);

		// graph = (CustomGraphView) findViewById(R.id.rlMainGraph);
		listGraphViewDataIndoor = new ArrayList<GraphViewData>();
		listGraphViewDataOutdoor = new ArrayList<GraphViewData>();
		// Get the real data and add here-

		for (int i = 0; i < listAQIIndoor.size(); i++) {
			listGraphViewDataIndoor.add(new GraphViewData(i, listAQIIndoor
					.get(i)));

		}

		for (int i = 0; i < listAQIOutdoor.size(); i++) {
			listGraphViewDataOutdoor.add(new GraphViewData(i, listAQIOutdoor
					.get(i)));
		}
		pagerGraph.setAdapter(new GraphAdapter(getSupportFragmentManager(),
				listGraphViewDataIndoor, listGraphViewDataOutdoor));

		// graph.setGraphData(listGraphViewDataIndoor,
		// listGraphViewDataOutdoor);

		ivLeftMenu = (ImageView) findViewById(R.id.ivBackIcon);
		ivLeftMenu.setOnClickListener(this);
		ivRightDevice = (ImageView) findViewById(R.id.ivRightDeviceIcon);
		ivRightDevice.setOnClickListener(this);

		// Indoor section values-
		tvDayLabel = (CustomTextView) findViewById(R.id.tvDayLabel);
		tvHomePercent = (CustomTextView) findViewById(R.id.tvHomePercent);
		tvAVGPMValue = (CustomTextView) findViewById(R.id.tvAVGPMValue);
		tvOnValue = (CustomTextView) findViewById(R.id.tvOnValue);

		// Outdoor Values --

		tvCity = (CustomTextView) findViewById(R.id.tvCity);
		tvAVGPMValueOutdoor = (CustomTextView) findViewById(R.id.tvAVGPMValueOutdoor);
		tvOutdoorPercent = (CustomTextView) findViewById(R.id.tvOutdoorPercent);

		// Legend
		tvCityLabel = (CustomTextView) findViewById(R.id.tvCityLabel);

	}

	private void updateUI() {
		tvCity.setText(sCityName);
		tvCityLabel.setText(sCityName);
		tvDayLabel.setText(Utils.getToday());
		tvHomePercent.setText(String.valueOf(Utils
				.getGoodAirPercentage(listAQIIndoor)));
		tvOutdoorPercent.setText(String.valueOf(Utils
				.getGoodAirPercentage(listAQIOutdoor)));
		tvAVGPMValue
				.setText(String.valueOf(Utils.getAverageAQI(listAQIIndoor)));
		tvAVGPMValueOutdoor.setText(String.valueOf(Utils
				.getAverageAQI(listAQIOutdoor)));
		// Hardcoded on Value -
		tvOnValue.setText("13hrs");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlDay:
			rlDay.requestFocus();
			// Populate the values for day-
			break;
		case R.id.rlWeek:
			rlWeek.requestFocus();
			// Populate the values for week-
			break;
		case R.id.rlMonth:
			rlMonth.requestFocus();
			// Populate the values for month-
			break;
		case R.id.ivBackIcon:
			finish();
			break;
		case R.id.ivRightDeviceIcon:
			startActivity(new Intent(this, SettingsActivity.class));
			finish();
			break;
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.rlDay:
			if (hasFocus)
				rlDay.setBackgroundResource(R.drawable.trend_day_selected);
			else
				rlDay.setBackgroundResource(R.drawable.trend_day_normal);
			break;
		case R.id.rlWeek:
			if (hasFocus)
				rlWeek.setBackgroundResource(R.drawable.trend_day_selected);
			else
				rlWeek.setBackgroundResource(R.drawable.trend_day_normal);
			break;
		case R.id.rlMonth:
			if (hasFocus)
				rlMonth.setBackgroundResource(R.drawable.trend_day_selected);
			else
				rlMonth.setBackgroundResource(R.drawable.trend_day_normal);
			break;

		default:
			break;
		}
	}

}

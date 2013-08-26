package com.philips.cl.di.dev.pa.screens;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.screens.customviews.CustomGraphView;
import com.philips.cl.di.dev.pa.screens.customviews.CustomGraphView.GraphViewData;

public class TrendsActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private RelativeLayout rlDay, rlWeek, rlMonth;
	private CustomGraphView graph;

	private List<GraphViewData> listGraphViewDataIndoor;
	private List<GraphViewData> listGraphViewDataOutdoor;
	private ImageView ivLeftMenu, ivRightDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trends);
		initialiseViews();
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

		graph = (CustomGraphView) findViewById(R.id.rlMainGraph);
		listGraphViewDataIndoor = new ArrayList<GraphViewData>();
		listGraphViewDataOutdoor = new ArrayList<GraphViewData>();
		// Get the real data and add here-
		/*
		 * for (int i = 0; i < 24; i++) { listGraphViewDataIndoor.add(new
		 * GraphViewData(i, Math.random() * 500));
		 * listGraphViewDataOutdoor.add(new GraphViewData(i, Math.random() *
		 * 500)); }
		 */

		graph.setGraphData(listGraphViewDataIndoor, listGraphViewDataOutdoor);

		ivLeftMenu = (ImageView) findViewById(R.id.ivBackIcon);
		ivLeftMenu.setOnClickListener(this);
		ivRightDevice = (ImageView) findViewById(R.id.ivRightDeviceIcon);
		ivRightDevice.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlDay:
			rlDay.requestFocus();
			break;
		case R.id.rlWeek:
			rlWeek.requestFocus();
			break;
		case R.id.rlMonth:
			rlMonth.requestFocus();
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

package com.philips.cl.di.dev.pa.screens;

import com.philips.cl.di.dev.pa.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;

public class TrendsActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private RelativeLayout rlDay, rlWeek, rlMonth;

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

		default:
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

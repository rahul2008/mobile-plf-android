package com.philips.cl.di.dev.pa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AirColorExplainedStaticActivity extends BaseActivity {

	private int activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent in = getIntent();
		activity = in.getIntExtra("AIR_QUALITY_ACTIVITY", -1);

		if (activity == AirQualityActivity.INDOOR_POLLUTANT_SCREEN) {
			setContentView(R.layout.indoor_pollutants_screen);
			initView(R.string.indoor_pollutants);
		} else if (activity == AirQualityActivity.VITASHIELD_SCREEN) {
			setContentView(R.layout.vita_shield_technology_screen);
			initView(R.string.vistashield_ips_system);
		} else if (activity == AirQualityActivity.GUARD_EVNVIRONMENT_SCREEN) {
			setContentView(R.layout.guards_environment_screen);
			initView(R.string.guards_environment);
		}
		
		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
		setBackground(scrollView);
	}
	
	private void initView(int headingStrId) {
		ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		FontTextView headingTV=(FontTextView) findViewById(R.id.heading_name_tv);
		headingTV.setText(getString(headingStrId));
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
}

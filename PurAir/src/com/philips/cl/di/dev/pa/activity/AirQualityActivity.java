package com.philips.cl.di.dev.pa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AirQualityActivity extends Activity implements OnClickListener {
	
	public static final int INDOOR_POLLUTANT_SCREEN = 11;
	public static final int VITASHIELD_SCREEN = 12;
	public static final int GUARD_EVNVIRONMENT_SCREEN = 13;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.AIR_QUALITY_EXPLAINED);
		setContentView(R.layout.air_quality_fragment);
		initializeView();
		
	}
	
	private void initializeView() {
		ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		FontTextView heading=(FontTextView) findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.list_item_air_quality_explained));
		FontTextView lblOutdoorColorExplained = (FontTextView) findViewById(R.id.lbl_outdoor_colors_explained);
		FontTextView lblIndoorColorExplained = (FontTextView) findViewById(R.id.lbl_indoor_colors_explained);
		FontTextView lblIndoorPollutant = (FontTextView) findViewById(R.id.lbl_indoor_pollutant);
		FontTextView lblVistashield = (FontTextView) findViewById(R.id.lbl_vistashield);
		FontTextView lblGuardEnv = (FontTextView) findViewById(R.id.lbl_guard_environment);
		
		lblOutdoorColorExplained.setOnClickListener(this);
		lblIndoorColorExplained.setOnClickListener(this);
		lblIndoorPollutant.setOnClickListener(this);
		lblVistashield.setOnClickListener(this);
		lblGuardEnv.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent in;
		switch (view.getId()) {
		case R.id.heading_back_imgbtn:
			//TODO : Back
			break;
		case R.id.lbl_outdoor_colors_explained:
			in = new Intent(AirQualityActivity.this, OutdoorAirColorIndicationActivity.class);
			startActivity(in);
			break;
		case R.id.lbl_indoor_colors_explained:
			in = new Intent(AirQualityActivity.this, IndoorAirColorIndicationActivity.class);
			startActivity(in);
			break;
		case R.id.lbl_indoor_pollutant:
			in = new Intent(AirQualityActivity.this, AirColorExplainedStaticActivity.class);
			in.putExtra("AIR_QUALITY_ACTIVITY", INDOOR_POLLUTANT_SCREEN);
			startActivity(in);
			break;
		case R.id.lbl_vistashield:
			in = new Intent(AirQualityActivity.this, AirColorExplainedStaticActivity.class);
			in.putExtra("AIR_QUALITY_ACTIVITY", VITASHIELD_SCREEN);
			startActivity(in);
			break;
		case R.id.lbl_guard_environment:
			in = new Intent(AirQualityActivity.this, AirColorExplainedStaticActivity.class);
			in.putExtra("AIR_QUALITY_ACTIVITY", GUARD_EVNVIRONMENT_SCREEN);
			startActivity(in);
			break;
		default:
			break;
		}
		
	}

}

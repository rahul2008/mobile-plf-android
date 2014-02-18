package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;

public class IndoorAQIAnalysisActivity  extends Activity {
	String outdoorTitle = "";
	String indoorTitle = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aqianalysis);
		outdoorTitle = getIntent().getStringExtra("outdoortitle");
		indoorTitle = getIntent().getStringExtra("indoortitle");
		
		((CustomTextView) findViewById(R.id.aqiAnalysisMsg11)).setText
			(getString(R.string.outdoor_analysis_detail2_head1)+" '"+indoorTitle+"'  " +
			getString(R.string.outdoor_analysis_detail2_head102)+" '"+outdoorTitle+"' " +
					getString(R.string.outdoor_analysis_detail2_head102));
	}
	
	public void closeDetils(View v) {
		finish();
	}

}
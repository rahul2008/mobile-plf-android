package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.philips.cl.di.dev.pa.R;

public class IndoorAQIAnalysisActivity  extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aqianalysis);
	}
	
	public void closeDetils(View v) {
		finish();
	}

}
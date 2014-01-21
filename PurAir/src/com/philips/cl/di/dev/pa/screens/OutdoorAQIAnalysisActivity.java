package com.philips.cl.di.dev.pa.screens;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class OutdoorAQIAnalysisActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.od_air_quality_explain);
	}
	
	public void closeDetils(View v) {
		finish();
	}

}

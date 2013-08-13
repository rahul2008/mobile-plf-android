package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.screens.customviews.FilterStatusView;

public class FilterDetailsActivity extends Activity implements OnClickListener {

	FilterStatusView fsvPreFilterBase, fsvMultiCareFilter,
			fsvActiveCarbonFilter, fsvhepaFilter;
	ImageView ivBackIcon ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filterdetails);
		overridePendingTransition(R.anim.in, R.anim.out);
		fsvPreFilterBase = (FilterStatusView) findViewById(R.id.ivPreFilterBase);
		fsvMultiCareFilter = (FilterStatusView) findViewById(R.id.ivMulticareBase);
		fsvActiveCarbonFilter = (FilterStatusView) findViewById(R.id.ivActiveFilterBase);
		fsvhepaFilter = (FilterStatusView) findViewById(R.id.ivHepaBase);
		ivBackIcon= (ImageView) findViewById(R.id.ivBackIcon);
		ivBackIcon.setOnClickListener(this);
		
		fsvActiveCarbonFilter.setFilterValue(100);
		fsvPreFilterBase.setFilterValue(140);
		fsvhepaFilter.setFilterValue(250);

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.in, R.anim.out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBackIcon:
			finish();
			break;

		default:
			break;
		}
		
	}

}

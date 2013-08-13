package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.os.Bundle;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.screens.customviews.FilterStatusView;

public class FilterDetailsActivity extends Activity {

	FilterStatusView fsvPreFilterBase, fsvMultiCareFilter,
			fsvActiveCarbonFilter, fsvhepaFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filterdetails);
		fsvPreFilterBase = (FilterStatusView) findViewById(R.id.ivPreFilterBase);
		fsvMultiCareFilter = (FilterStatusView) findViewById(R.id.ivMulticareBase);
		fsvActiveCarbonFilter = (FilterStatusView) findViewById(R.id.ivActiveFilterBase);
		fsvhepaFilter = (FilterStatusView) findViewById(R.id.ivHepaBase);
		
		fsvActiveCarbonFilter.setFilterValue(100);
		fsvPreFilterBase.setFilterValue(140);
		fsvhepaFilter.setFilterValue(250);

	}

}

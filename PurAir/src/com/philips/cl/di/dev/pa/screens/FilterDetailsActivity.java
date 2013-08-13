package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.FilterStatusDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.screens.customviews.FilterStatusView;
import com.philips.cl.di.dev.pa.utils.Utils;

public class FilterDetailsActivity extends Activity implements OnClickListener {

	FilterStatusView fsvPreFilterBase, fsvMultiCareFilter,
			fsvActiveCarbonFilter, fsvhepaFilter;
	ImageView ivBackIcon ; 
	
	private CustomTextView tvPreFilter,tvHepaFilter,tvMultiCareFilter,tvActiveCarbonFilter ;

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
		
		tvPreFilter = (CustomTextView) findViewById(R.id.tvPreFilterStatus) ;
		tvHepaFilter = (CustomTextView) findViewById(R.id.tvHepaFilterStatus) ;
		tvMultiCareFilter = (CustomTextView)findViewById(R.id.tvMultiCareFilterStatus) ;
		tvActiveCarbonFilter = (CustomTextView) findViewById(R.id.tvActiveFilterStatus) ;
		
		updateFilterStatusValue() ;
	}
	
	
	private void updateFilterStatusValue() {
		FilterStatusDto filterStatusDto = SessionDto.getInstance().getFilterStatusDto() ;
		if( filterStatusDto != null ) {
			
			int activeCarbonFilterValue = filterStatusDto.getActiveCarbonFilterStatus() ;
			int hepaFilterValue = filterStatusDto.getHepaFilterStatus() ;
			int multiCareFilterValue = filterStatusDto.getMultiCareFilterStatus() ;
			int preFilterValue = filterStatusDto.getPreFilterStatus() ;
			
			fsvActiveCarbonFilter.setFilterValue(activeCarbonFilterValue);
			fsvPreFilterBase.setFilterValue(preFilterValue);
			fsvhepaFilter.setFilterValue(hepaFilterValue);
			fsvMultiCareFilter.setFilterValue(multiCareFilterValue) ;
			
			tvPreFilter.setText(Utils.getTimeRemaining(preFilterValue)) ;
			tvPreFilter.setTextColor(Utils.getFilterStatusColor(preFilterValue)) ;
			
			tvHepaFilter.setText(Utils.getTimeRemaining(hepaFilterValue)) ;
			tvHepaFilter.setTextColor(Utils.getFilterStatusColor(hepaFilterValue)) ;
			
			tvMultiCareFilter.setText(Utils.getTimeRemaining(multiCareFilterValue)) ;
			tvMultiCareFilter.setTextColor(Utils.getFilterStatusColor(multiCareFilterValue)) ;
			
			tvActiveCarbonFilter.setText(Utils.getTimeRemaining(activeCarbonFilterValue)) ;
			tvActiveCarbonFilter.setTextColor(Utils.getFilterStatusColor(activeCarbonFilterValue)) ;
		}
		// Set default
		else {
			fsvActiveCarbonFilter.setFilterValue(100);
			fsvPreFilterBase.setFilterValue(140);
			fsvhepaFilter.setFilterValue(250);
		}
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

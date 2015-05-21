package com.philips.cl.di.dev.pa.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.TutorialPagerAdapter;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialPagerActivity extends BaseActivity {

	private TutorialPagerAdapter mAdapter;
	private ViewPager mPager;

	

	private int mCurrentItemId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tutorial_pager_layout);
		
		mAdapter = new TutorialPagerAdapter(getSupportFragmentManager(), this);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setSnap(true);

		final float density = getResources().getDisplayMetrics().density;
		indicator.setPageColor(0xFF5D6577);
		indicator.setFillColor(0xFFB9BBC7);
		indicator.setStrokeWidth(0.1f * density);

		mPager.setCurrentItem(mCurrentItemId);
		indicator.setCurrentItem(mCurrentItemId);
		MetricsTracker.trackPage(TrackPageConstants.APP_TUTORIAL + "Page " + mCurrentItemId);
		ImageButton backButton = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		FontTextView heading=(FontTextView) findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.app_tutorial));
		
		ImageButton cancelButton = (ImageButton) findViewById(R.id.heading_close_imgbtn);
		cancelButton.setVisibility(View.VISIBLE);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.APP_TUTORIAL);
	}
}

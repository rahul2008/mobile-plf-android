package com.philips.cl.di.dev.pa.activity;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ViewPagerAdapter;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialPagerActivity extends BaseActivity {

	private ViewPagerAdapter mAdapter;
	private ViewPager mPager;

	private static final int[] TITLE_LIST = new int[] {
			R.string.tutorial_title_1, R.string.tutorial_title_2,
			R.string.tutorial_title_3, R.string.tutorial_title_4,
			R.string.tutorial_title_5, R.string.tutorial_title_6 };

	private int mCurrentItemId = -1;
	private FontTextView headingTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mCurrentItemId = intent.getIntExtra(AirTutorialActivity.SELECTED_PAGE, -1);
		setContentView(R.layout.tutorial_pager_layout);
		initView();

		mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setSnap(true);

		final float density = getResources().getDisplayMetrics().density;
		indicator.setPageColor(0xFF5D6577);
		indicator.setFillColor(0xFFB9BBC7);
		indicator.setStrokeWidth(0.1f * density);

		if (mCurrentItemId != -1) {
			mPager.setCurrentItem(mCurrentItemId);
			setActionBarTitle(TITLE_LIST[mCurrentItemId]);
			indicator.setCurrentItem(mCurrentItemId);
			MetricsTracker.trackPage(TrackPageConstants.APP_TUTORIAL + "Page " + mCurrentItemId);
		}

		// to change ActionBar title on each swipe
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				setActionBarTitle(TITLE_LIST[position]);
				MetricsTracker.trackPage(TrackPageConstants.APP_TUTORIAL + "Page " + position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.APP_TUTORIAL);
	}
	
	private void initView() {
		ImageButton closeButton = (ImageButton) findViewById(R.id.heading_close_imgbtn);
		closeButton.setVisibility(View.VISIBLE);
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TutorialPagerActivity.this.finish();
			}
		});
		headingTV=(FontTextView) findViewById(R.id.heading_name_tv);
	}

	/* Sets Action bar title */
	private void setActionBarTitle(int tutorialTitle) {
		// If Chinese language selected set font-type-face normal
		if (LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			headingTV.setTypeface(Typeface.DEFAULT);
		}
		headingTV.setText(this.getText(tutorialTitle));
	}

}

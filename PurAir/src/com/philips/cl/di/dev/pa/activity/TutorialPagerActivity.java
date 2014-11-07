package com.philips.cl.di.dev.pa.activity;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ViewPagerAdapter;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialPagerActivity extends BaseActivity {

	private ViewPagerAdapter mAdapter;
	private ViewPager mPager;

	private static final int[] TITLE_LIST = new int[] {
			R.string.tutorial_title_1, R.string.tutorial_title_2,
			R.string.tutorial_title_3, R.string.tutorial_title_4,
			R.string.tutorial_title_5, R.string.tutorial_title_6 };

	private int mCurrentItemId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mCurrentItemId = intent.getIntExtra(AirTutorialActivity.SELECTED_PAGE,
				-1);
		setContentView(R.layout.tutorial_pager_layout);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.MAINACTIVITY, "Actionbar: " + e.getMessage());
		}

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
		}

		// to change ActionBar title on each swipe
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				setActionBarTitle(TITLE_LIST[position]);
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
	
	/* Initialize action bar */
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d = getResources().getDrawable(R.drawable.ews_nav_bar_2x);
		actionBar.setBackgroundDrawable(d);
		View view = getLayoutInflater().inflate(R.layout.home_action_bar, null);
		((ImageView) view.findViewById(R.id.right_menu_img))
				.setImageResource(R.drawable.close_icon_blue);
		((ImageView) view.findViewById(R.id.left_menu_img))
				.setVisibility(View.GONE);
		((ImageView) view.findViewById(R.id.back_to_home_img))
				.setVisibility(View.GONE);
		((ImageView) view.findViewById(R.id.add_location_img))
				.setVisibility(View.GONE);
		actionBar.setCustomView(view);
		setActionBarTitle(R.string.tutorial_title_1);

		((ImageView) view.findViewById(R.id.right_menu_img))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						TutorialPagerActivity.this.finish();
					}
				});
	}

	/* Sets Action bar title */
	public void setActionBarTitle(int tutorialTitle) {
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		// If Chinese language selected set font-type-face normal
		if (LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains(
				"ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault())
						.contains("ZH-HANT")) {
			textView.setTypeface(Typeface.DEFAULT);
		} else {
			textView.setTypeface(Fonts.getGillsansLight(this));
		}
//		textView.setTextSize(24);
		textView.setText(this.getText(tutorialTitle));
	}

}

package com.philips.cl.di.dev.pa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ViewPagerAdapter;
import com.philips.cl.di.dev.pa.utils.Fonts;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialPagerActivity extends BaseActivity {

	private ViewPagerAdapter mAdapter;
	private ViewPager mPager;

	private ActionBar mActionBar;

	private static final int[] TITLE_LIST= new int[]{
		R.string.tutorial_title_1,
		R.string.tutorial_title_2,
		R.string.tutorial_title_3,
		R.string.tutorial_title_4,
		R.string.tutorial_title_5,
		R.string.tutorial_title_6
	};

	private int mCurrentItemId=-1;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent= getIntent();
		mCurrentItemId=intent.getIntExtra(AirTutorialActivity.SELECTED_PAGE, -1);
		setContentView(R.layout.tutorial_pager_layout);
		initActionBar();

		mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);		

		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setSnap(true);

		final float density = getResources().getDisplayMetrics().density;
		indicator.setPageColor(0xFF5D6577);
		indicator.setFillColor(0xFFB9BBC7);   
		indicator.setStrokeWidth(0.1f*density);	

		if(mCurrentItemId!=-1)
		{
			mPager.setCurrentItem(mCurrentItemId);
			setActionBarTitle(TITLE_LIST[mCurrentItemId]);
			indicator.setCurrentItem(mCurrentItemId);
		}

		//to change ActionBar title on each swipe
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				setActionBarTitle(TITLE_LIST[position]);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}


	/*Initialize action bar */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		mActionBar.setCustomView(R.layout.action_bar);	
		setActionBarTitle(R.string.tutorial_title_1);
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int tutorialTitle) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(this));
		textView.setTextSize(24);
		textView.setText(this.getText(tutorialTitle));
	}

	/*Sets the right menu*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.getItem(0);		
		item.setIcon(R.drawable.close_icon_blue);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		switch (item.getItemId()) {
		case R.id.right_menu:
			TutorialPagerActivity.this.finish();
			break;
		default:
			break;
		}
		return false;
	}
}

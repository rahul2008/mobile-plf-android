package com.philips.cl.di.dev.pa.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ExpandableListAdapter;
import com.philips.cl.di.dev.pa.util.Fonts;

public class OutdoorAirColorIndicationActivity extends BaseActivity {

	/**
	 * strings for group elements
	 */
	private static final int ARR_GROUP_ELEMENTS[] ={
		R.string.hazardous,
		R.string.very_unhealthy,
		R.string.unhealthy,
		R.string.unhealthy_for_sensitive_groups,
		R.string.moderate,
		R.string.good
	};

	/**
	 * strings for child elements
	 */
	private static final int ARR_CHILD_ELEMENTS[][] =
		{
		{
			R.string.outdoor_hazardous
		},
		{
			R.string.outdoor_unhealthy
		},
		{
			R.string.outdoor_moderately_polluted
		},
		{
			R.string.outdoor_slightly_polluted
		},
		{
			R.string.outdoor_moderate
		},
		{
			R.string.outdoor_good
		}
		};

	private static final int COLOR_LIST[]={
		R.drawable.circle_1,
		R.drawable.circle_2,
		R.drawable.circle_3,
		R.drawable.circle_4,
		R.drawable.circle_5,
		R.drawable.circle_6
	};

	private static final String COLOR_LIST_LABEL[]={
		"500+", "300","200","150","100","50"
	};

	private ExpandableListView expListView;
	private ExpandableListAdapter listAdapter;

	private ActionBar mActionBar;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_quality_indication);
		initActionBar();
		TextView indoorColorExplained=(TextView) findViewById(R.id.color_explained_intro);
		indoorColorExplained.setText(R.string.outdoor_color_explained_intro);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.colors_list); 
		listAdapter = new ExpandableListAdapter(this, ARR_GROUP_ELEMENTS, ARR_CHILD_ELEMENTS,COLOR_LIST,COLOR_LIST_LABEL);
		setGroupIndicatorToRight();
		expListView.setAdapter(listAdapter);
	}
	
	/*Initialize action bar */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		mActionBar.setCustomView(R.layout.action_bar);	
		setActionBarTitle(R.string.outdoor_colors_explained);
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int title) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(this));
		textView.setTextSize(24);
		textView.setText(this.getText(title));
	}

	@TargetApi(18)
	private void setGroupIndicatorToRight() {
		/* Get the screen width */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			expListView.setIndicatorBounds(width - getDipsFromPixel(60), width
					- getDipsFromPixel(30));
		} else {
			expListView.setIndicatorBoundsRelative(width - getDipsFromPixel(60), width
					- getDipsFromPixel(30));
		}		
	}

	// Convert pixel to dip
	public int getDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}
}

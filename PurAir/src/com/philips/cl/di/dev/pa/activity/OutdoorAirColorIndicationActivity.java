package com.philips.cl.di.dev.pa.activity;

import java.util.Locale;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ExpandableListAdapter;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class OutdoorAirColorIndicationActivity extends BaseActivity {

	/**
	 * strings for group elements
	 */
	private static final int ARR_GROUP_ELEMENTS[] ={
		R.string.hazardous_outdoor,
		R.string.unhealthy_outdoor,
		R.string.moderately_polluted_outdoor,
		R.string.slightly_polluted_outdoor,
		R.string.moderate_outdoor,
		R.string.good_outdoor
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
		"300+", "201-300","151-200","101-150","51-100","0-50"
	};

	private ExpandableListView expListView;
	private ExpandableListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_quality_indication);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.MAINACTIVITY, "Actionbar: " + "Error: " + e.getMessage());
		}
		TextView indoorColorExplained=(TextView) findViewById(R.id.color_explained_intro);
		indoorColorExplained.setText(R.string.outdoor_color_explained_intro);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.colors_list); 
		listAdapter = new ExpandableListAdapter(this, ARR_GROUP_ELEMENTS, ARR_CHILD_ELEMENTS,COLOR_LIST,COLOR_LIST_LABEL);
		setGroupIndicatorToRight();
		expListView.setAdapter(listAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_AIR_QUALITY_COLOURS_EXPLAINED);
	}
	
	/*Initialize action bar */
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view = getLayoutInflater().inflate(R.layout.home_action_bar, null);
		((ImageView)view.findViewById(R.id.right_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.left_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.back_to_home_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.add_location_img)).setVisibility(View.GONE);
		actionBar.setCustomView(view);
		setActionBarTitle(R.string.outdoor_colors_explained);
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int title) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			textView.setTypeface(Typeface.DEFAULT);
		} else {
			textView.setTypeface(Fonts.getGillsansLight(this));
		}
//		textView.setTextSize(24);
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

package com.philips.cl.di.dev.pa.pureairui.fragments;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.screens.adapters.ExpandableListAdapter;
import com.philips.cl.di.dev.pa.util.Fonts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class IndoorAirColorIndicationFragment extends Fragment {


	/**
	 * strings for group elements
	 */
	private static final int arrGroupelements[] ={
		R.string.very_unhealthy,
		R.string.unhealthy,
		R.string.moderate,
		R.string.good
	};

	/**
	 * strings for child elements
	 */
	private static final int arrChildelements[][] ={
		{
			R.string.indoor_very_unhealthy
		},
		{
			R.string.indoor_unhealthy
		},		
		{
			R.string.indoor_moderate
		},
		{
			R.string.indoor_good
		}
	};

	private static final int colorList[]={
		R.drawable.circle_1,
		R.drawable.circle_2,
		R.drawable.circle_3,
		R.drawable.circle_6
	};

	private static final String colorListLabel[]={
		"10+", "4","3","2"
	};

	private ExpandableListView expListView;
	private ExpandableListAdapter listAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.air_quality_indication, container, false);
		TextView indoorColorExplained=(TextView) view.findViewById(R.id.color_explained_intro);
		indoorColorExplained.setTypeface(Fonts.getGillsans(getActivity()));
		indoorColorExplained.setText(R.string.indoor_color_explained_intro);

		// get the listview
		expListView = (ExpandableListView) view.findViewById(R.id.colors_list); 
		listAdapter = new ExpandableListAdapter(getActivity(), arrGroupelements, arrChildelements,colorList,colorListLabel);
		expListView.setAdapter(listAdapter);
		setGroupIndicatorToRight();

		MainActivity mainActivity=(MainActivity) getActivity();
		mainActivity.disableNavigationIndicator();

		return view; 
	}

	private void setGroupIndicatorToRight() {
		//Get the screen width 
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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

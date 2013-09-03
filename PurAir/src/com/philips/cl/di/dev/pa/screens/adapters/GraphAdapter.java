package com.philips.cl.di.dev.pa.screens.adapters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.customviews.CustomGraphView.GraphViewData;
import com.philips.cl.di.dev.pa.screens.fragments.GraphFragment;

public class GraphAdapter extends FragmentStatePagerAdapter {
	private ArrayList<GraphViewData> listGraphViewDataIndoor;
	private ArrayList<GraphViewData> listGraphViewDataOutdoor;

	public GraphAdapter(FragmentManager fm,
			List<GraphViewData> listGraphViewDataIndoor,
			List<GraphViewData> listGraphViewDataOutdoor) {
		super(fm);
		this.listGraphViewDataIndoor = (ArrayList<GraphViewData>) listGraphViewDataIndoor;
		this.listGraphViewDataOutdoor = (ArrayList<GraphViewData>) listGraphViewDataOutdoor;
		
	}

	@Override
	public Fragment getItem(int pos) {
		Fragment f = new GraphFragment();
		Bundle args = new Bundle();
		// TODO
		args.putParcelableArrayList(AppConstants.INDOOR,listGraphViewDataIndoor);
		args.putParcelableArrayList(AppConstants.OUTDOOR, listGraphViewDataOutdoor);
		f.setArguments(args);
		return f;
	}

	@Override
	public int getCount() {
		return 1;
	}

}

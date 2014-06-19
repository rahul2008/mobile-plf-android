package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<OutdoorFragment> outdoorFragments;
	private FragmentManager fragmentManager;

	public OutdoorPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragmentManager = fm;
		outdoorFragments = new ArrayList<OutdoorFragment>();
	}

	@Override
	public Fragment getItem(int position) {
		ALog.i(ALog.DASHBOARD, "OutdoorPagerAdapter$getItem position " + position);
		Bundle bundle = new Bundle();
		bundle.putInt("position", position) ;
		OutdoorFragment fragment = new OutdoorFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		ALog.i(ALog.DASHBOARD, "OutdoorPagerAdapter$getItemPosition " + object);
		return POSITION_NONE;
	}
	
	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
	    return ((Fragment)object).getView() == view;
	}
	
	@Override
	public Parcelable saveState() {
		return null;
	}

}

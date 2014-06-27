package com.philips.cl.di.dev.pa.dashboard;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorPagerAdapter extends FragmentStatePagerAdapter {
	
	private int count;
	
	public OutdoorPagerAdapter(FragmentManager fm, int count) {
		super(fm);
		this.count = count ;
	}
	
	public void mCount(int count) {
		this.count = count ;
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
		return count ;
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

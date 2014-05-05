package com.philips.cl.di.dev.pa.dashboard;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorPagerAdapter extends FragmentStatePagerAdapter {

	public OutdoorPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		ALog.i(ALog.DASHBOARD, "OutdoorPagerAdapter$getItem position " + position);
		return OutdoorFragment.newInstance(OutdoorManager.getInstance().getOutdoorDashboardData(position));
	}

	@Override
	public int getItemPosition(Object object) {
		ALog.i(ALog.DASHBOARD, "OutdoorPagerAdapter$getItemPosition " + object);
		return POSITION_NONE;
	}
	
	@Override
	public int getCount() {
		return 20;
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

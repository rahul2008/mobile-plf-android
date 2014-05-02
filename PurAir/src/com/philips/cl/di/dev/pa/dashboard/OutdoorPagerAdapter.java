package com.philips.cl.di.dev.pa.dashboard;

import com.philips.cl.di.dev.pa.util.ALog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OutdoorPagerAdapter extends FragmentPagerAdapter {

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

}

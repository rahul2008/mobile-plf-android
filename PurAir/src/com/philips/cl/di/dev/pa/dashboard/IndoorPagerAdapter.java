package com.philips.cl.di.dev.pa.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IndoorPagerAdapter extends FragmentPagerAdapter {
	
	public IndoorPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
		return new IndoorFragment();
	}

	@Override
	public int getCount() {
		return 1;
	}

}

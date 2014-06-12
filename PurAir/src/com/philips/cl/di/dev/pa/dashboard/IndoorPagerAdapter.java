package com.philips.cl.di.dev.pa.dashboard;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.philips.cl.di.dev.pa.util.ALog;

public class IndoorPagerAdapter extends FragmentStatePagerAdapter {

	public IndoorPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		ALog.i(ALog.DASHBOARD, "IndoorPagerAdapter$getItem");
		return new IndoorFragment();
	}

	@Override
	public int getCount() {
		return 1;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		ALog.i(ALog.DASHBOARD, "Object in indoor view: "+object.toString()) ;
	    return ((Fragment)object).getView() == view;
	}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
}

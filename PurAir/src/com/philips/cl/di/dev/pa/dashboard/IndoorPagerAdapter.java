package com.philips.cl.di.dev.pa.dashboard;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.util.ALog;

public class IndoorPagerAdapter extends FragmentStatePagerAdapter implements OnPageChangeListener{
	
	private int position;
	
	public IndoorPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
		ALog.i(ALog.DASHBOARD, "IndoorPagerAdapter$getItem position: " + position);
		if( this.position < DiscoveryManager.getInstance().getDevicesFromDB().size()) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", this.position) ;
			IndoorFragment fragment = new IndoorFragment();
			fragment.setArguments(bundle);
			return fragment;
		}
		else {
			return new AddPurifierFragment() ;
		}
		
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	@Override
	public int getCount() {
		return DiscoveryManager.getInstance().getDevicesFromDB().size()+1; 
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		ALog.i(ALog.DASHBOARD, "IndoorPagerAdapter$onPageSelected position " + position);
		this.position = position;
		notifyDataSetChanged();
	}
}

package com.philips.cl.di.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the primary sections of the app.
 */ 
public class DiSectionsPagerAdapter extends FragmentPagerAdapter {
	private int mCount;
	private String mPrefix;

	public DiSectionsPagerAdapter(FragmentManager fm, Context context,String prefix, int pageCount) {
		super(fm);
		mCount = pageCount;
		mPrefix = prefix;
	}


	@Override
	public Fragment getItem(int index) {
		DiPageFragment f =new DiPageFragment();
		 Bundle args = new Bundle();
		 args.putInt("page", index);
		 args.putString("prefix", mPrefix);
		 f.setArguments(args);
		return f;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
		
	}
}

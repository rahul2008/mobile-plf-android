package com.philips.cdp.di.iap.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cdp.di.iap.Fragments.DotNavigationTestFragment;

public class ViewPagerAdaptor extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[]{"Page 1", "Page 2", "Page 3", "Page 4",};
    private int mCount = CONTENT.length;

    public ViewPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DotNavigationTestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ViewPagerAdaptor.CONTENT[position % CONTENT.length];
    }
}
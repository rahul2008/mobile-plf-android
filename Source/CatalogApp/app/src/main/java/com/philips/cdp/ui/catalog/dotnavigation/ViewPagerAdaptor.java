package com.philips.cdp.ui.catalog.dotnavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cdp.ui.catalog.cardviewpager.CardsTestFragment;


public class ViewPagerAdaptor extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[]{"Page 1", "Page 2", "Page 3", "Page 4",};
    private int type;
    private int mCount = CONTENT.length;

    public ViewPagerAdaptor(FragmentManager fm, int type) {
        super(fm);
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        if (type == 0)
        return DotNavigationTestFragment.newInstance(CONTENT[position % CONTENT.length]);
        else
            return CardsTestFragment.newInstance();
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
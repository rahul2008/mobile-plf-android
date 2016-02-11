package com.philips.productselection.productscreen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.productselection.productscreen.NavigationFragment;

/**
 * This is the adapter class to keep all the image contents storing
 *
 * @author naveen@philips.com
 * @Date 28/01/2016
 */
public class ProductAdapter extends FragmentPagerAdapter {


    protected static final String[] CONTENT = new String[]{"one", "two", "three", "four", "five", "Six", "Seven"};

    private int mCount = CONTENT.length;


    public ProductAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return NavigationFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ProductAdapter.CONTENT[position % CONTENT.length];
    }
}

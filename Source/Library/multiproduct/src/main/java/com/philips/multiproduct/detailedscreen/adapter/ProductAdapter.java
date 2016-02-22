package com.philips.multiproduct.detailedscreen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.multiproduct.detailedscreen.NavigationFragment;

/**
 * This is the adapter class to keep all the image contents storing
 *
 * @author naveen@philips.com
 * @Date 28/01/2016
 */
public class ProductAdapter extends FragmentPagerAdapter {


    protected static String[] CONTENT = null;


    public ProductAdapter(FragmentManager fm, String[] images) {
        super(fm);
        CONTENT = new String[images.length];
        CONTENT = images;
    }

    @Override
    public Fragment getItem(int position) {
        return NavigationFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ProductAdapter.CONTENT[position % CONTENT.length];
    }
}

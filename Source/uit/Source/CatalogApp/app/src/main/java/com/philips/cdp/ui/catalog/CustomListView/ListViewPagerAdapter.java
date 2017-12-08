package com.philips.cdp.ui.catalog.CustomListView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.cdp.ui.catalog.activity.TabFragmentListOption;
import com.philips.cdp.ui.catalog.activity.TabFragmentListProduct;
import com.philips.cdp.ui.catalog.activity.TabFragmentListWithoutIcon1;
import com.philips.cdp.ui.catalog.activity.TabFragmentListicon;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ListViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public ListViewPagerAdapter(final FragmentManager fm) {
        super(fm);
    }


    public ListViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                TabFragmentListOption tab1 = new TabFragmentListOption();
                return tab1;
            case 1:
                TabFragmentListProduct tab2 = new TabFragmentListProduct();
                return tab2;
            case 2:
                TabFragmentListicon tab3 = new TabFragmentListicon();
                return tab3;
            case 3:
                TabFragmentListWithoutIcon1 tab4 = new TabFragmentListWithoutIcon1();
                return tab4;


            default:
                TabFragmentListOption tab6 = new TabFragmentListOption();
                return tab6;

        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

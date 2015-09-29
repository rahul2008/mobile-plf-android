package com.philips.cdp.ui.catalog.imagenavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.viewpagerindicator.DotNavigationTestFragment;
import com.viewpagerindicator.IconsAdaptor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserAdapter extends IconsAdaptor {

    public UserAdapter(final FragmentManager fm, final int[] ICONS) {
        super(fm, ICONS);
    }

    @Override
    public Fragment getItem(final int position) {
        return DotNavigationTestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }
}

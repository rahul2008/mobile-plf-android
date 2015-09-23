package com.philips.cdp.ui.catalog.image_navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.dot_navigation.DotNavigationTestFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class IconPagerAdaptor extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "Page 1", "Page 2", "Page 3", "Page 4", };

    private int mCount = CONTENT.length;

    protected static final int[] ICONS = new int[] {
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location
    };

    public IconPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DotNavigationTestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getIconResId(final int index) {
        return ICONS[index % ICONS.length];
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return IconPagerAdaptor.CONTENT[position % CONTENT.length];
    }

}
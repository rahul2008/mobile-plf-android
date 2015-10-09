package com.viewpagerindicator;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class IconsAdaptor extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "Page 1", "Page 2", "Page 3", "Page 4"};

    private int mCount = CONTENT.length;

    private int[] ICONS;

    public IconsAdaptor(FragmentManager fm, int[] ICONS) {
        super(fm);
        this.ICONS = ICONS;
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
      return IconsAdaptor.CONTENT[position % CONTENT.length];
    }

}
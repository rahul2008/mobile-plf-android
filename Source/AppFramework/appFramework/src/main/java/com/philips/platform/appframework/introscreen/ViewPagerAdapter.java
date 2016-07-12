/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[]{"Page 1", "Page 2", "Page 3"};
    private int mCount = CONTENT.length;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return IntroductionFragmentStart.newInstance(0, "");
            case 1:
                return IntroductionFragmentStart.newInstance(1, "");
            case 2:
                return IntroductionFragmentStart.newInstance(2, "");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ViewPagerAdapter.CONTENT[position % CONTENT.length];
    }
}
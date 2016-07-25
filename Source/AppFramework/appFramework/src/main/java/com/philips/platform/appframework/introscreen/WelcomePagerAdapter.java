/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class WelcomePagerAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[]{"Page 1", "Page 2", "Page 3"};
    private static final int FIRST_INSTANCE = 0;
    private static final int SECOND_INSTANCE = 1;
    private static final int THIRD_INSTANCE = 2;
    private int mCount = CONTENT.length;

    public WelcomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return WelcomeFragment.newInstance(FIRST_INSTANCE, "");
            case 1:
                return WelcomeFragment.newInstance(SECOND_INSTANCE, "");
            case 2:
                return WelcomeFragment.newInstance(THIRD_INSTANCE, "");
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
        return WelcomePagerAdapter.CONTENT[position % CONTENT.length];
    }
}
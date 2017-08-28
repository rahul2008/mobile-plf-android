/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.uappdemo.screens.introscreen.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.platform.uappdemolibrary.R;

/**
 * Adapter is made for Showing instances of Welcome Fragment
 * If onboarding screens need to be changed those changes should be done in this fragment
 * Addition and removal of new screen should be done here
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter {

    private static final String[] CONTENT = new String[] { "Page 1", "Page 2", "Page 3" };
    private final int PAGER_POSITION_ONE = 0;
    private final int PAGER_POSITION_TWO = 1;
    private final int PAGER_POSITION_THREE = 2;
    public WelcomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case PAGER_POSITION_ONE:
                return WelcomePagerFragment.newInstance(R.string.introduction_screen_one_bottom_text,
                        R.string.introduction_screen_one_bottom_text, R.drawable.af_welcome_start_page_bg);
            case PAGER_POSITION_TWO:
                return WelcomePagerFragment.newInstance(R.string.introduction_screen_two_bottom_text,
                        R.string.introduction_screen_two_bottom_text, R.drawable.af_welcome_center_page_bg);
            case PAGER_POSITION_THREE:
                return WelcomePagerFragment.newInstance(R.string.introduction_screen_three_bottom_text,
                        R.string.introduction_screen_three_bottom_text, R.drawable.af_welcome_end_page_bg);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return WelcomePagerAdapter.CONTENT[position % CONTENT.length];
    }
}
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;

/**
 * Adapter is made for Showing instances of Welcome Fragment
 * If onboarding screens need to be changed those changes should be done in this fragment
 * Addition and removal of new screen should be done here
 */
public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG =  WelcomePagerAdapter.class.getSimpleName();

    private static final String[] CONTENT = new String[] { "Page 1", "Page 2", "Page 3", "Page 4","Page 5", "Page 6", "Page 7", "Page 8","Page 9" };
    private final int PAGER_POSITION_ONE = 0;
    private final int PAGER_POSITION_TWO = 1;
    private final int PAGER_POSITION_THREE = 2;
    private final int PAGER_POSITION_FOUR = 3;
    private final int PAGER_POSITION_FIVE = 4;
    private final int PAGER_POSITION_SIX = 5;
    private final int PAGER_POSITION_SEVEN = 6;
    private final int PAGER_POSITION_EIGHT = 7;
    private final int PAGER_POSITION_NINE = 8;

    public WelcomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        RALog.d(TAG,"getItem");
        switch (position) {
            case PAGER_POSITION_ONE:
                return new WelcomeVideoPagerFragment();
            case PAGER_POSITION_TWO:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen2_title,
                        R.string.RA_DLS_onboarding_screen2_sub_text, R.mipmap.onboarding_screen_2);
            case PAGER_POSITION_THREE:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen3_title,
                        R.string.RA_DLS_onboarding_screen3_sub_text, R.mipmap.onboarding_screen_3);
            case PAGER_POSITION_FOUR:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen4_title,
                        R.string.RA_DLS_onboarding_screen4_sub_text, R.mipmap.onboarding_screen_4);
            case PAGER_POSITION_FIVE:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen5_title,
                        R.string.RA_DLS_onboarding_screen5_sub_text, R.mipmap.onboarding_screen_5);
            case PAGER_POSITION_SIX:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen6_title,
                        R.string.RA_DLS_onboarding_screen6_sub_text, R.mipmap.onboarding_screen_6);
            case PAGER_POSITION_SEVEN:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen7_title,
                        R.string.RA_DLS_onboarding_screen7_sub_text, R.mipmap.onboarding_screen_7);
            case PAGER_POSITION_EIGHT:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen8_title,
                        R.string.RA_DLS_onboarding_screen8_sub_text, R.mipmap.onboarding_screen_8);
            case PAGER_POSITION_NINE:
                return WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen9_title,
                        R.string.RA_DLS_onboarding_screen9_sub_text, R.mipmap.onboarding_screen_9);
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
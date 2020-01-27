/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;

/**
 * Adapter is made for Showing instances of Welcome Fragment
 * If onboarding screens need to be changed those changes should be done in this fragment
 * Addition and removal of new screen should be done here
 */
public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = WelcomePagerAdapter.class.getSimpleName();

    private static final String[] CONTENT = new String[]{"Page 1", "Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9"};

    private final int[] titleResIds = new int[]{R.string.RA_DLS_onboarding_screen1_title,
            R.string.RA_DLS_onboarding_screen2_title, R.string.RA_DLS_onboarding_screen3_title,
            R.string.RA_DLS_onboarding_screen4_title, R.string.RA_DLS_onboarding_screen5_title,
            R.string.RA_DLS_onboarding_screen6_title, R.string.RA_DLS_onboarding_screen7_title,
            R.string.RA_DLS_onboarding_screen8_title, R.string.RA_DLS_onboarding_screen9_title};

    private final int[] subTitleResIds = new int[]{R.string.RA_DLS_onboarding_screen1_sub_text,
            R.string.RA_DLS_onboarding_screen2_sub_text, R.string.RA_DLS_onboarding_screen3_sub_text,
            R.string.RA_DLS_onboarding_screen4_sub_text, R.string.RA_DLS_onboarding_screen5_sub_text,
            R.string.RA_DLS_onboarding_screen6_sub_text, R.string.RA_DLS_onboarding_screen7_sub_text,
            R.string.RA_DLS_onboarding_screen8_sub_text, R.string.RA_DLS_onboarding_screen9_sub_text};

    private final int[] drawableResIds = new int[]{R.drawable.onboarding_01,
            R.drawable.onboarding_01, R.drawable.onboarding_02, R.drawable.onboarding_03,
            R.drawable.onboarding_04, R.drawable.onboarding_05, R.drawable.onboarding_06,
            R.drawable.onboarding_07, R.drawable.onboarding_08};

    private final int[] groupResIds = new int[]{R.drawable.group1,R.drawable.group1,
            R.drawable.group2, R.drawable.group3, R.drawable.group4,
            R.drawable.group5, R.drawable.group6, R.drawable.group7,
            R.drawable.group8};


    public WelcomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        RALog.d(TAG, "getItem");
        if (!isValidPosition(position)) {
            return null;
        }

        if (position == 0) {
            return new WelcomeVideoPagerFragment();
        }

        return WelcomePagerFragment.newInstance(titleResIds[position],
                subTitleResIds[position], drawableResIds[position],groupResIds[position]);
    }

    private boolean isValidPosition(int position) {
        return position < getCount();
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
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.platform.ths.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter is made for Showing instances of Welcome Fragment
 * If onboarding screens need to be changed those changes should be done in this fragment
 * Addition and removal of new screen should be done here
 */
class OnBoardingTourPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = OnBoardingTourPagerAdapter.class.getSimpleName();

    private final List<OnBoardingTourContentModel> onBoardingTourContentModelList;
    private final Context context;


    OnBoardingTourPagerAdapter(FragmentManager fragmentManager, List<OnBoardingTourContentModel> onBoardingTourContentModelList, Context context) {
        super(fragmentManager);
        this.onBoardingTourContentModelList = onBoardingTourContentModelList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return OnBoardingTourPageFragment.newInstance(onBoardingTourContentModelList.get(position).getTourPageTextId(),
                onBoardingTourContentModelList.get(position).getTourBackgroundDrawable(), onBoardingTourContentModelList.get(position).getSpanValues());
    }

    private boolean isValidPosition(int position) {
        return position < getCount();
    }

    @Override
    public int getCount() {
        return onBoardingTourContentModelList.size();
    }

    @Override
    public String getPageTitle(int position) {
        return onBoardingTourContentModelList.get(position).getPageTitle();
    }
}
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.List;

/**
 * Adapter is made for Showing instances of Welcome Fragment
 * If onboarding screens need to be changed those changes should be done in this fragment
 * Addition and removal of new screen should be done here
 */
class OnBoardingTourPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = OnBoardingTourPagerAdapter.class.getSimpleName();

    private final List<OnBoardingTourContentModel> onBoardingTourContentModelList;
    private FragmentLauncher fragmentLauncher;
    private ActionBarListener actionBarListener;


    OnBoardingTourPagerAdapter(FragmentManager fragmentManager, List<OnBoardingTourContentModel> onBoardingTourContentModelList, FragmentLauncher fragmentLauncher, ActionBarListener actionBarListener) {
        super(fragmentManager);
        this.onBoardingTourContentModelList = onBoardingTourContentModelList;
        this.fragmentLauncher = fragmentLauncher;
        this.actionBarListener = actionBarListener;
    }

    @Override
    public THSBaseFragment getItem(int position) {
        final THSBaseFragment thsBaseFragment = OnBoardingTourPageFragment.newInstance(onBoardingTourContentModelList.get(position).getTourPageTextId(),
                onBoardingTourContentModelList.get(position).getTourBackgroundDrawable(),
                onBoardingTourContentModelList.get(position).getIsAmwellLogoVisible(),
                onBoardingTourContentModelList.get(position).getTitleId());
        thsBaseFragment.setFragmentLauncher(fragmentLauncher);
        thsBaseFragment.setActionBarListener(actionBarListener);
        return thsBaseFragment;
    }

    @Override
    public int getCount() {
        return onBoardingTourContentModelList.size();
    }

    @Override
    public String getPageTitle(int position) {
        String pageTitle = onBoardingTourContentModelList.get(position).getPageTitle();
        StringBuilder stringBuilder = new StringBuilder(pageTitle);
        if(THSManager.getInstance().getOnBoradingABFlow().equalsIgnoreCase(THSConstants.THS_ONBOARDING_ABFLOW1) ) {
            stringBuilder =  stringBuilder.append(" Onboarding");
            return stringBuilder.toString();
        }else {
            stringBuilder =  stringBuilder.append("_alt Onboarding");
            return stringBuilder.toString();
         }
    }
}
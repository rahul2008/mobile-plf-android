/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.mya.profile.MyaProfileFragment;
import com.philips.platform.mya.settings.MyaSettingsFragment;


public class MyaPager extends FragmentStatePagerAdapter {

    private int tabCount;
    private MyaBaseFragment myaBaseFragment;
    private Fragment tabConfiguredFragment;

    public MyaPager(FragmentManager fm, int tabCount, MyaBaseFragment myaBaseFragment) {
        super(fm);
        this.tabCount= tabCount;
        this.myaBaseFragment = myaBaseFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyaProfileFragment myaProfileFragment = new MyaProfileFragment();
                myaProfileFragment.setArguments(myaBaseFragment.getArguments());
                myaProfileFragment.setActionbarUpdateListener(myaBaseFragment.getActionbarUpdateListener());
                myaProfileFragment.setFragmentLauncher(myaBaseFragment.getFragmentLauncher());
                return myaProfileFragment;
            case 1:
                MyaSettingsFragment myaSettingsFragment = new MyaSettingsFragment();
                myaSettingsFragment.setArguments(myaBaseFragment.getArguments());
                myaSettingsFragment.setActionbarUpdateListener(myaBaseFragment.getActionbarUpdateListener());
                myaSettingsFragment.setFragmentLauncher(myaBaseFragment.getFragmentLauncher());
                return myaSettingsFragment;
            case 2:
                return tabConfiguredFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void setTabConfiguredFragment(Fragment tabConfiguredFragment) {
        this.tabConfiguredFragment = tabConfiguredFragment;
    }
}

/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.profile.MyaProfileFragment;
import com.philips.platform.mya.settings.MyaSettingsFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


public class MyaPager extends FragmentStatePagerAdapter {

    private int tabCount;
    private AppInfra appInfra;
    private FragmentLauncher fragmentLauncher;
    private MyaListener myaListener;

    public MyaPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyaProfileFragment();
            case 1:
                return new MyaSettingsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void setAppInfra(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }

    public void setMyaListener(MyaListener myaListener) {
        this.myaListener = myaListener;
    }
}

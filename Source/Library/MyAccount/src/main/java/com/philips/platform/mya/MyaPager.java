/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.platform.mya.profile.MyaProfileFragment;
import com.philips.platform.mya.settings.MyaSettingsFragment;


public class MyaPager extends FragmentStatePagerAdapter {

    private int tabCount;
    private Bundle arguments;

    public MyaPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyaProfileFragment myaProfileFragment = new MyaProfileFragment();
                myaProfileFragment.setArguments(arguments);
                return myaProfileFragment;
            case 1:
                MyaSettingsFragment myaSettingsFragment = new MyaSettingsFragment();
                myaSettingsFragment.setArguments(arguments);
                return myaSettingsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }
}

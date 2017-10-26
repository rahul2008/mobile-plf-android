package com.philips.platform.mya.mock;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class FragmentActivityMock extends FragmentActivity {

    private FragmentManagerMock fragmentManagerMock;

    public FragmentActivityMock(FragmentManagerMock fragmentManagerMock) {
        this.fragmentManagerMock = fragmentManagerMock;
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return fragmentManagerMock;
    }

}

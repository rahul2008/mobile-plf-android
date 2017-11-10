package com.philips.platform.mya.wrapper;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.mya.MyaFragment;

public class MyaFragmentWrapper extends MyaFragment {
    public FragmentActivity fragmentActivity;

    @Override
    protected FragmentActivity overridableGetActivity() {
        return fragmentActivity;
    }

}

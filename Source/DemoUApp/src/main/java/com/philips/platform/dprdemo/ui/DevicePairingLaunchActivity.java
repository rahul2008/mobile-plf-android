/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.dprdemo.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class DevicePairingLaunchActivity extends UiKitActivity implements ActionBarListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_pairing_launch_layout);

        showFragment();
    }

    public void showFragment() {
        int containerId = R.id.dpr_frame_container;

        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, containerId, this);
        DevicePairingBaseFragment pairingFragment = new PairingFragment();
        pairingFragment.showFragment(pairingFragment, fragmentLauncher);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.dpr_frame_container);
        if (currentFrag instanceof PairingFragment) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        setTitle(getResources().getString(i));
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        setTitle(s);
    }
}

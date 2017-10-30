/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class BluelibUappActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bll_uapp_activity_main);

        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, R.id.cml_uapp_activity_main, null);

        BluelibUapp.get().launch(fragmentLauncher, null);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

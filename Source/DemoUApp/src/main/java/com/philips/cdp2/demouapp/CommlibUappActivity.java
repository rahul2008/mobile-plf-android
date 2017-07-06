/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp2.commlib.demouapp.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class CommlibUappActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uapp_activity_main);

        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, R.id.uapp_activity_main, null);
        CommlibUapp commlibUapp = CommlibUapp.instance;
        commlibUapp.init(new DefaultCommlibUappDependencies(getApplicationContext()), new UappSettings(getApplicationContext()));
        commlibUapp.launch(fragmentLauncher, null);
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

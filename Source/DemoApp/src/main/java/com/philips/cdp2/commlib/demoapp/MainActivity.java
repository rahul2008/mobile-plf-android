/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.demoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.DefaultCommlibUappDependencies;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, R.id.activity_main, null);
        CommlibUapp commlibUapp = CommlibUapp.instance;
        commlibUapp.init(new DefaultCommlibUappDependencies(getApplicationContext()), null);
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

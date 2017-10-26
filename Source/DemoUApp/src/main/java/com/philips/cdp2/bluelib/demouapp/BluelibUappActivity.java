/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp2.bluelib.demouapp.fragment.about.AboutFragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bll_menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            AboutFragment.newInstance().show(getSupportFragmentManager(), "AboutFragment");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.ui.catalog.PlaceholderFragment;
import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertTest extends CatalogActivity {

    private Bundle savedInstanceState;
    private PlaceholderFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.modal_alert_frag);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new PlaceholderFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fragment != null && fragment.isShowing())
            outState.putBoolean("dialogState", true);

        fragment.dismiss();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState != null && savedInstanceState.getBoolean("dialogState")) {
                    fragment.show();
                }
            }
        }, 100);
    }

}
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.ui.catalog.PlaceholderFragment;
import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertTest extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_alert_frag);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new PlaceholderFragment())
                .commit();
    }

}
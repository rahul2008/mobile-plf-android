package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.ModalAlertDemoFragment;
import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertDemo extends CatalogActivity {

    private Bundle savedInstanceState;
    private ModalAlertDemoFragment modalAlertDemoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.modal_alert_demo);

        Button showAlert = (Button) findViewById(R.id.show_modal_dialog);
        showAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalAlertDemoFragment = new ModalAlertDemoFragment();
                modalAlertDemoFragment.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (modalAlertDemoFragment != null && modalAlertDemoFragment.isVisible()) {
            outState.putBoolean("dialogState", true);
            modalAlertDemoFragment.dismiss();
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState != null && savedInstanceState.getBoolean("dialogState")) {
                    modalAlertDemoFragment = new ModalAlertDemoFragment();
                    modalAlertDemoFragment.show(getSupportFragmentManager(), "dialog");
                }
            }
        }, 100);
    }

}
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PhilipsDialog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertDemo extends CatalogActivity {
    private PhilipsDialog alert;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.modal_alert_demo);
        findViewById(R.id.show_modal_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                alert = new PhilipsDialog(ModalAlertDemo.this);
                alert.setContentView(R.layout.uikit_modal_alert);
                Button justOnce = (Button) alert.findViewById(R.id.dialogButtonCancel);
                Button always = (Button) alert.findViewById(R.id.dialogButtonOK);
                justOnce.setOnClickListener(ModalAlertDemo.this.onClick(alert));
                always.setOnClickListener(ModalAlertDemo.this.onClick(alert));
                alert.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState != null && savedInstanceState.getBoolean("dialogState")) {
                    alert = new PhilipsDialog(ModalAlertDemo.this);
                    alert.setContentView(R.layout.uikit_modal_alert);
                    Button justOnce = (Button) alert.findViewById(R.id.dialogButtonCancel);
                    Button always = (Button) alert.findViewById(R.id.dialogButtonOK);
                    justOnce.setOnClickListener(ModalAlertDemo.this.onClick(alert));
                    always.setOnClickListener(ModalAlertDemo.this.onClick(alert));
                    alert.show();
                }
            }
        }, 100);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (alert != null && alert.isShowing())
            outState.putBoolean("dialogState", true);
    }

    @NonNull
    private View.OnClickListener onClick(final PhilipsDialog alert) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        };
    }


}

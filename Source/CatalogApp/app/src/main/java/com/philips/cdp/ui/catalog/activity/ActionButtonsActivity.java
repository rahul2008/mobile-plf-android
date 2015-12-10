package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.UiKitModalAlert;

public class ActionButtonsActivity extends CatalogActivity {
    private UiKitModalAlert alert;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_action_buttons);
    }

    public void onClick(View view) {
        alert = new UiKitModalAlert(ActionButtonsActivity.this);
        alert.setContentView(R.layout.uikit_modal_alert);
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState != null && savedInstanceState.getBoolean("dialogState")) {
                    alert = new UiKitModalAlert(ActionButtonsActivity.this);
                    alert.setContentView(R.layout.uikit_modal_alert);
                    Button justOnce = (Button) alert.findViewById(R.id.dialogButtonCancel);
                    Button always = (Button) alert.findViewById(R.id.dialogButtonOK);
                    justOnce.setOnClickListener(ActionButtonsActivity.this.onClick(alert));
                    always.setOnClickListener(ActionButtonsActivity.this.onClick(alert));
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
    private View.OnClickListener onClick(final UiKitModalAlert alert) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        };
    }

}

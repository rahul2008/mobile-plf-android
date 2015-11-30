package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PhilipsDialog;

public class ActionButtonsActivity extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_buttons);
    }

    public void onClick(View view) {
        PhilipsDialog alert = new PhilipsDialog(ActionButtonsActivity.this);
        alert.setContentView(R.layout.uikit_modal_alert);
        alert.show();
    }
}

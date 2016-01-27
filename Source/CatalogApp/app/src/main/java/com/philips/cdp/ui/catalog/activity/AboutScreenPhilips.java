package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AboutScreenPhilips extends UiKitActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_about_screen);
        setActionBar(getSupportActionBar());
        findViewById(R.id.UpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
        findViewById(R.id.aboutscreen_close).setVisibility(View.GONE);
    }

    private void setActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.uikit_about_screen_actionbar);
        }
    }
}

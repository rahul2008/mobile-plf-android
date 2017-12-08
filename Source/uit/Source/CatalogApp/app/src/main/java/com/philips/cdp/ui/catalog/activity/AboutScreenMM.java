package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AboutScreenMM extends UiKitActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_about_screen_mm);
        setActionBar(getSupportActionBar());
        findViewById(R.id.CloseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        ImageView logo = (ImageView) findViewById(R.id.about_screen_logo);
        logo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.uikit_makers_logo, null));
        findViewById(R.id.aboutscreen_back).setVisibility(View.GONE);
        findViewById(R.id.about_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutScreenMM.this, com.philips.cdp.ui.catalog.activity.DummyActivityForListItemClick.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.about_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutScreenMM.this, com.philips.cdp.ui.catalog.activity.DummyActivityForListItemClick.class);
                startActivity(intent);
            }
        });
    }

    private void setActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.uikit_about_screen_actionbar_mm);
        }
    }
}

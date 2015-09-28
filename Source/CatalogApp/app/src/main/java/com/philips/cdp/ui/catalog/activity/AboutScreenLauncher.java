package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AboutScreenLauncher extends CatalogActivity{

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen_choices);
    }

    public void launchAboutScreen(View v) {
        startActivity(getAboutScreenIntent(v.getId()));
    }

    public Intent getAboutScreenIntent(int id) {
        Class<?> targetClass = AboutScreenPhilips.class;

        switch (id) {
            case R.id.about_screen_philips:
                targetClass = AboutScreenPhilips.class;
                break;
            case R.id.about_screen_mm:
                targetClass = AboutScreenMM.class;
                break;
        }
        return new Intent(this, targetClass);
    }
}

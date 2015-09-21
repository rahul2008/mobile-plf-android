package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.activity.CatalogActivity;
import com.philips.cdp.ui.catalog.activity.SplashActivityGradient;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashLauncher extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_buttons);
    }

    public void launchSplashScreen(View v) {
        startActivity(getSplashIntent(v.getId()));
    }

    public Intent getSplashIntent(int id) {
        Class<?> targetClass = SplashActivityLogoTop.class;

        switch (id) {
            case R.id.lt:
                targetClass = SplashActivityLogoTop.class;
                break;
            case R.id.lc_tb:
                targetClass = SplashActivityLogoCenterTitleBottom.class;
                break;
            case R.id.lb:
                targetClass = SplashActivityLogoBottom.class;
                break;
            case R.id.grad:
                targetClass = SplashActivityGradient.class;
                break;

        }
        return new Intent(this, targetClass);
    }
}

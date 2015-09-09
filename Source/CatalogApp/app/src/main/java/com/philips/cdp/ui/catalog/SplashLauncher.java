package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashLauncher extends AppCompatActivity {

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
            case R.id.lc_tt:
                targetClass = SplashActivityLogoCenterTitleTop.class;
                break;
            case R.id.lc_tb:
                targetClass = SplashActivityLogoCenterTitleBottom.class;
                break;
            case R.id.lb:
                targetClass = SplashActivityLogoBottom.class;
                break;
        }
        return new Intent(this, targetClass);
    }
}

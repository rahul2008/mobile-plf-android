package com.philips.cdp.ui.catalog;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.philips.cdp.ui.catalog.activity.CatalogActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashActivityLogoBottom extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_bottom);
//        findViewById(R.id.splash_layout).setBackground(ResourcesCompat.getDrawable(this, R.drawable.uikit_food));
        ViewGroup group = (ViewGroup) findViewById(R.id.splash_layout);
        group.setBackgroundResource(R.drawable.uikit_food);


    }
}

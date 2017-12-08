package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashActivityGradient extends UiKitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_bottom);

        ViewGroup parent = (ViewGroup) findViewById(R.id.splash_title).getParent();
        parent.setBackgroundResource(R.drawable.uikit_grad_light_blue_background);

        ImageView logo = (ImageView) findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_philips_logo));
    }
}

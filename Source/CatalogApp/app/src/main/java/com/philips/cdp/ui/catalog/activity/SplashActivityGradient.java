package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashActivityGradient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(com.philips.cdp.uikit.R.layout.splash_screen_logo_bottom);

        ViewGroup parent = (ViewGroup) findViewById(R.id.splash_title).getParent();
        parent.setBackgroundResource(R.drawable.grad_light_blue_background);
    }


}

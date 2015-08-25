package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 310213764 on 25-08-2015.
 */
public class SplashLauncher extends AppCompatActivity {

    public final static String SPLASH_MODE = "splash_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_buttons);
    }

    public void launchSplashScreen(View v) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.putExtra(SPLASH_MODE, v.getId());
            startActivity(intent);
    }
}

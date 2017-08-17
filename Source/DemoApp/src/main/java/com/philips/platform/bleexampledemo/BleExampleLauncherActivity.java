/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.bleexampledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppInterface;
import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppSettings;
import com.example.cdpp.bluelibexampleapp.uapp.DefaultBleDemoMicroAppDependencies;
import com.facebook.stetho.Stetho;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class BleExampleLauncherActivity extends AppCompatActivity {

    private Button launchButton;

    private boolean launchMicroAppUsingButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (launchMicroAppUsingButton) {
            setContentView(R.layout.activity_ble_example_launcher);
            launchButton = (Button) findViewById(R.id.ble_example_launch_button);
            launchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchBleMicroApp();
                }
            });
        } else {
            launchBleMicroApp();
        }

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    private void launchBleMicroApp() {
        BleDemoMicroAppInterface uAppInterface = BleDemoMicroAppInterface.getInstance();
        uAppInterface.init(new DefaultBleDemoMicroAppDependencies(this.getApplicationContext()), new BleDemoMicroAppSettings(getApplicationContext()));
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }
}

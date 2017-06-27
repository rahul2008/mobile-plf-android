/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.bleexampledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppInterface;
import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppSettings;
import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppDependencies;
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
    }

    private void launchBleMicroApp() {
        BleDemoMicroAppInterface uAppInterface = new BleDemoMicroAppInterface();
        uAppInterface.init(new BleDemoMicroAppDependencies(null), new BleDemoMicroAppSettings(getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }
}

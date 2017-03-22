package com.philips.platform.ccdemouapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class CCDemoUAppLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccdemo_uapp);

        CCDemoUAppuAppInterface uAppInterface = new CCDemoUAppuAppInterface();
        uAppInterface.init(new CCDemoUAppuAppDependencies(CCDemoApplication.gAppInfra), new CCDemoUAppuAppSettings(this));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required

    }

}

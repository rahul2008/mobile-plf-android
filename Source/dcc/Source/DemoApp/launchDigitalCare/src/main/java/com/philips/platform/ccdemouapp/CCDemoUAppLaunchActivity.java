package com.philips.platform.ccdemouapp;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class CCDemoUAppLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccdemo_uapp);

        CCDemoUAppuAppInterface uAppInterface = new CCDemoUAppuAppInterface();
        uAppInterface.init(new CCDemoUAppuAppDependencies(CCDemoApplication.gAppInfra), new CCDemoUAppuAppSettings(this));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(this,ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,null, 0,null), null);// pass launch input if required

    }

}

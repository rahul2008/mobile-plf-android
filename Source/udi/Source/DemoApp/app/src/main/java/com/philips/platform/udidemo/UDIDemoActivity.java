package com.philips.platform.udidemo;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.udi.demouapp.UdiDemoAppSettings;
import com.udi.demouapp.UdiDemoUAppDependencies;
import com.udi.demouapp.UdiDemoUAppInterface;
import com.udi.demouapp.UdiLaunchInput;

public class UDIDemoActivity extends Activity {

    private UdiDemoUAppInterface iapDemoUAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);
    }

    public void launch(View v) {
        UDIDemoApplication demoApplication = (UDIDemoApplication) getApplicationContext();
        AppInfra appInfra = demoApplication.getAppInfra();
        iapDemoUAppInterface = new UdiDemoUAppInterface();
        iapDemoUAppInterface.init(new UdiDemoUAppDependencies(appInfra), new UdiDemoAppSettings(this));
        iapDemoUAppInterface.launch(new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, null, 0, null), new UdiLaunchInput());
    }
}

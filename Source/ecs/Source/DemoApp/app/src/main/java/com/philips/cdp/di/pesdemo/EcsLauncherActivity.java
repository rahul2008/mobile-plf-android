package com.philips.cdp.di.pesdemo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;


import com.ecs.demotestuapp.TestAPI;
import com.ecs.demotestuapp.integration.EcsDemoTestAppSettings;
import com.ecs.demotestuapp.integration.EcsDemoTestUAppDependencies;
import com.ecs.demotestuapp.integration.EcsDemoTestUAppInterface;
import com.ecs.demotestuapp.integration.EcsTestLaunchInput;
import com.ecs.demouapp.integration.EcsDemoAppSettings;
import com.ecs.demouapp.integration.EcsDemoUAppDependencies;
import com.ecs.demouapp.integration.EcsDemoUAppInterface;
import com.ecs.demouapp.integration.EcsLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

/**
 * Created by philips on 6/16/17.
 */

public class EcsLauncherActivity extends Activity {

    private EcsDemoUAppInterface iapDemoUAppInterface;

    private EcsDemoTestUAppInterface iapDemoTestUAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pes_activity_luncher);
    }

    public void launch(View v) {
        EcsDemoApplication pesDemoApplication = (EcsDemoApplication) getApplicationContext();
        AppInfra appInfra = pesDemoApplication.getAppInfra();
        iapDemoUAppInterface = new EcsDemoUAppInterface();
        iapDemoUAppInterface.init(new EcsDemoUAppDependencies(appInfra), new EcsDemoAppSettings(this));
        iapDemoUAppInterface.launch(new ActivityLauncher(this,ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,null, 0,null), new EcsLaunchInput());
    }


    public void launchTest(View view) {

        EcsDemoApplication pesDemoApplication = (EcsDemoApplication) getApplicationContext();
        AppInfra appInfra = pesDemoApplication.getAppInfra();
        iapDemoTestUAppInterface = new EcsDemoTestUAppInterface();
        iapDemoTestUAppInterface.init(new EcsDemoTestUAppDependencies(appInfra), new EcsDemoTestAppSettings(this));
        iapDemoTestUAppInterface.launch(new ActivityLauncher(this,ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,null, 0,null), new EcsTestLaunchInput());
    }
}

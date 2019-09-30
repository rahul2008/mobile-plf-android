package com.philips.cdp.di.iandemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ian.demouapp.IanDemoAppSettings;
import com.ian.demouapp.IanDemoUAppDependencies;
import com.ian.demouapp.IanDemoUAppInterface;
import com.ian.demouapp.IanLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;


public class LuncherActivity extends Activity {

    private IanDemoUAppInterface iapDemoUAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);
    }

    public void launch(View v) {
        DemoApplication demoApplication = (DemoApplication) getApplicationContext();
        AppInfra appInfra = demoApplication.getAppInfra();
        iapDemoUAppInterface = new IanDemoUAppInterface();
        iapDemoUAppInterface.init(new IanDemoUAppDependencies(appInfra), new IanDemoAppSettings(this));
        iapDemoUAppInterface.launch(new ActivityLauncher(this,ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,null, 0,null), new IanLaunchInput());
    }
}

package com.philips.cdp.di.iapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iap.demouapp.DemoAppActivity;
import com.iap.demouapp.IapDemoAppSettings;
import com.iap.demouapp.IapDemoUAppDependencies;
import com.iap.demouapp.IapDemoUAppInterface;
import com.iap.demouapp.IapLaunchInput;
import com.iap.demouapp.TestClass;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * Created by philips on 6/16/17.
 */

public class LuncherActivity extends AppCompatActivity {

    private DemoApplication demoApplication;
    private AppInfra appInfra;
    private IapDemoUAppInterface iapDemoUAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        demoApplication=(DemoApplication)getApplicationContext();
        appInfra = demoApplication.getAppInfra();
        iapDemoUAppInterface=new IapDemoUAppInterface();
        iapDemoUAppInterface.init(new IapDemoUAppDependencies(appInfra),new IapDemoAppSettings(this));
        setContentView(R.layout.activity_luncher);
    }

    public void launch(View v){
        iapDemoUAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,0),new IapLaunchInput());

    }
}

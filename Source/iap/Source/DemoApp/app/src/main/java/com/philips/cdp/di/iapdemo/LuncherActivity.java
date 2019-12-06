package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.iap.demouapp.IapDemoAppSettings;
import com.iap.demouapp.IapDemoUAppDependencies;
import com.iap.demouapp.IapDemoUAppInterface;
import com.iap.demouapp.IapLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

/**
 * Created by philips on 6/16/17.
 */

public class LuncherActivity extends Activity {

    private IapDemoUAppInterface iapDemoUAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);
    }

    public void launch(View v) {
        DemoApplication demoApplication = (DemoApplication) getApplicationContext();
        AppInfra appInfra = demoApplication.getAppInfra();
        iapDemoUAppInterface = new IapDemoUAppInterface();
        iapDemoUAppInterface.init(new IapDemoUAppDependencies(appInfra), new IapDemoAppSettings(this));
        iapDemoUAppInterface.launch(new ActivityLauncher(this,ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,null, 0,null), new IapLaunchInput());
    }
}

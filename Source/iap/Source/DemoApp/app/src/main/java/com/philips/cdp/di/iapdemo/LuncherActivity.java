package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.iap.demouapp.IapDemoAppSettings;
import com.iap.demouapp.IapDemoUAppDependencies;
import com.iap.demouapp.IapDemoUAppInterface;
import com.iap.demouapp.IapLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.ThemeUtils;

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

        int themeResourceID = ThemeUtils.getThemeResourceID(this);

        DemoApplication demoApplication = (DemoApplication) getApplicationContext();
        AppInfra appInfra = demoApplication.getAppInfra();
        appInfra.getServiceDiscovery().setHomeCountry("US");
        iapDemoUAppInterface = new IapDemoUAppInterface();
        iapDemoUAppInterface.init(new IapDemoUAppDependencies(appInfra), new IapDemoAppSettings(this));
        iapDemoUAppInterface.launch(new ActivityLauncher(this,ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,null, themeResourceID,null), new IapLaunchInput());
    }
}

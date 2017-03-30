package com.philips.platform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uappdemo.R;
import com.philips.platform.uappdemo.UappDemouAppDependencies;
import com.philips.platform.uappdemo.UappDemouAppInterface;
import com.philips.platform.uappdemo.UappDemouAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;


public class UappActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demoactivity);
        UappDemouAppInterface uAppInterface = new UappDemouAppInterface();
        UappDemoApplication uappDemoApplication = (UappDemoApplication)getApplicationContext();
        UappDemouAppDependencies uappDependencies = new UappDemouAppDependencies(uappDemoApplication.getAppInfra());
        uAppInterface.init(uappDependencies, new UappDemouAppSettings(this));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required
        finish();
    }
}

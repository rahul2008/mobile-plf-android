package com.philips.platform.aildemolaunch;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.aildemo.AILDemouAppDependencies;
import com.philips.platform.aildemo.AILDemouAppInterface;
import com.philips.platform.aildemo.AILDemouAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class AppInfraLaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_infra_launch);
        invokeMicroApp();
        finish();
    }


    private void invokeMicroApp() {
        AILDemouAppInterface uAppInterface = AILDemouAppInterface.getInstance();
        AppInfraApplication appInfraApplication = (AppInfraApplication)getApplication();
        uAppInterface.init(new AILDemouAppDependencies(appInfraApplication.getAppInfra()), new AILDemouAppSettings(getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required
    }
}

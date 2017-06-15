package com.philips.platform.aildemolaunch;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.philips.platform.aildemo.AILDemouAppDependencies;
import com.philips.platform.aildemo.AILDemouAppInterface;
import com.philips.platform.aildemo.AILDemouAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class AppInfraLaunchActivity extends AppCompatActivity implements View.OnClickListener {
    Button appInfraLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_infra_launch);
        appInfraLaunch=(Button) findViewById(R.id.appinfra);
        appInfraLaunch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AILDemouAppInterface uAppInterface = new AILDemouAppInterface();
        uAppInterface.init(new AILDemouAppDependencies(AppInfraApplication.gAppInfra), new AILDemouAppSettings(getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required
    }
}

package com.philips.platform.prdemoapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;


public class PRDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdemoapp);
        findViewById(R.id.launch_pr_demo_app_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PRDemoAppuAppInterface uAppInterface = new PRDemoAppuAppInterface();
                uAppInterface.init(new PRDemoAppuAppDependencies(PRUiHelper.getInstance().getAppInfraInstance()), new PRDemoAppuAppSettings(PRDemoActivity.this));// pass App-infra instance instead of null
                uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required

            }
        });

    }

}

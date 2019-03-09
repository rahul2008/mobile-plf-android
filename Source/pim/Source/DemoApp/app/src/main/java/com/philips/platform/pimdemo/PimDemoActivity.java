package com.philips.platform.pimdemo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Button;
import com.pim.demouapp.DemoAppActivity;
import com.pim.demouapp.PimDemoAppSettings;
import com.pim.demouapp.PimDemoUAppDependencies;
import com.pim.demouapp.PimDemoUAppInterface;
import com.pim.demouapp.PimLaunchInput;

public class PimDemoActivity extends UIDActivity {

    private PimDemoUAppInterface uAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urdemo);
        uAppInterface = new PimDemoUAppInterface();
        Button changeTheme = findViewById(R.id.launch);
        changeTheme.setOnClickListener(v -> {
            Intent intent = new Intent(PimDemoActivity.this, DemoAppActivity.class);
            startActivity(intent);
        });

        AppInfraInterface appInfraInterface = PimDemoApplication.getInstance().getAppInfra();
        uAppInterface.init(new PimDemoUAppDependencies(appInfraInterface), new PimDemoAppSettings(this.getApplicationContext()));
    }

    public void launch(View v) {
        uAppInterface.launch(new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, null, 0, null), new PimLaunchInput());
    }
}

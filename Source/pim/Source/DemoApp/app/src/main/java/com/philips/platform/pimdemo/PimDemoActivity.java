package com.philips.platform.pimdemo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Button;
import com.pim.demouapp.PIMDemoUAppActivity;
import com.pim.demouapp.PIMDemoUAppSettings;
import com.pim.demouapp.PIMDemoUAppDependencies;
import com.pim.demouapp.PIMDemoUAppInterface;
import com.pim.demouapp.PIMDemoUAppLaunchInput;

public class PimDemoActivity extends UIDActivity {

    private PIMDemoUAppInterface uAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pim_demo);
        uAppInterface = new PIMDemoUAppInterface();
        Button changeTheme = findViewById(R.id.launch);
        changeTheme.setOnClickListener(v -> {
            Intent intent = new Intent(PimDemoActivity.this, PIMDemoUAppActivity.class);
            startActivity(intent);
        });

        AppInfraInterface appInfraInterface = PimDemoApplication.getInstance().getAppInfra();
        uAppInterface.init(new PIMDemoUAppDependencies(appInfraInterface), new PIMDemoUAppSettings(this.getApplicationContext()));
    }

    public void launch(View v) {
        uAppInterface.launch(new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, null, 0, null), new PIMDemoUAppLaunchInput());
    }
}

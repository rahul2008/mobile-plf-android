package com.philips.platform.pimdemo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Button;
import com.udi.demouapp.DemoAppActivity;
import com.udi.demouapp.UdiDemoAppSettings;
import com.udi.demouapp.UdiDemoUAppDependencies;
import com.udi.demouapp.UdiDemoUAppInterface;
import com.udi.demouapp.UdiLaunchInput;

public class PimDemoActivity extends UIDActivity {

    private PimDemoUAppInterface uAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urdemo);
        uAppInterface = new UdiDemoUAppInterface();
        Button changeTheme = findViewById(R.id.launch);
        changeTheme.setOnClickListener(v -> {
            Intent intent = new Intent(UDIDemoActivity.this, DemoAppActivity.class);
            startActivity(intent);
        });

        AppInfraInterface appInfraInterface = UDIDemoApplication.getInstance().getAppInfra();
        uAppInterface.init(new UdiDemoUAppDependencies(appInfraInterface), new UdiDemoAppSettings(this.getApplicationContext()));
    }

    public void launch(View v) {
        uAppInterface.launch(new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, null, 0, null), new UdiLaunchInput());
    }
}

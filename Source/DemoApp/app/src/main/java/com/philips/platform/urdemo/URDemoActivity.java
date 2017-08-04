package com.philips.platform.urdemo;


import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import com.philips.platform.appinfra.*;
import com.philips.platform.uappframework.launcher.*;

public class URDemoActivity extends AppCompatActivity {

    private URDemouAppInterface uAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urdemo);
        uAppInterface = new URDemouAppInterface();
        AppInfraInterface appInfraInterface = URDemoApplication.getInstance().getAppInfra();
        uAppInterface.init(new URDemouAppDependencies(appInfraInterface), new URDemouAppSettings(this.getApplicationContext()));
        setStandardFlow();
    }

    private void setStandardFlow() {
        TextView standardFlow = (TextView) findViewById(R.id.usrdemo_mainScreen_standard_text);
        standardFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
            }
        });
    }

}

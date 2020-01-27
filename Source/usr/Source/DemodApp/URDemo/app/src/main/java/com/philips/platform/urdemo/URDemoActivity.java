package com.philips.platform.urdemo;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class URDemoActivity extends AppCompatActivity {

    private URDemouAppInterface uAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urdemo);
        uAppInterface = new URDemouAppInterface();
        AppInfraInterface appInfraInterface = new AppInfra.Builder().build(this.getApplicationContext());
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

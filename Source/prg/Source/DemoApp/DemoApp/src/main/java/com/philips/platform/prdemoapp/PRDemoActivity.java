package com.philips.platform.prdemoapp;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;


public class PRDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.setDebugLogging(true);
        setContentView(R.layout.activity_prdemoapp);
        findViewById(R.id.launch_pr_demo_app_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PRDemoAppuAppInterface uAppInterface = new PRDemoAppuAppInterface();
                uAppInterface.init(new PRDemoAppuAppDependencies(PRUiHelper.getInstance().getAppInfraInstance()), new PRDemoAppuAppSettings(PRDemoActivity.this));// pass App-infra instance instead of null
                ActivityLauncher uiLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, getDlsThemeConfiguration(),R.style.Theme_DLS_Green_VeryDark, null);
                uAppInterface.launch(uiLauncher, null);// pass launch input if required

            }
        });
    }

    @NonNull
    private ThemeConfiguration getDlsThemeConfiguration() {
        return new ThemeConfiguration(this, ColorRange.BLUE, NavigationColor.VERY_DARK, ContentColor.VERY_DARK, AccentRange.ORANGE);
    }

}

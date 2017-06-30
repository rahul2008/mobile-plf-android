package com.philips.platform;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uappdemo.R;
import com.philips.platform.uappdemo.UappDemoDependencies;
import com.philips.platform.uappdemo.UappDemoInterface;
import com.philips.platform.uappdemo.UappDemoSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;


public class UappActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demoactivity);
        UappDemoInterface uAppInterface = new UappDemoInterface();
        UappDemoApplication uappDemoApplication = (UappDemoApplication) getApplicationContext();
        UappDemoDependencies uappDependencies = new UappDemoDependencies(uappDemoApplication.getAppInfra());
        uAppInterface.init(uappDependencies, new UappDemoSettings(this));// pass App-infra instance instead of null
        ActivityLauncher uiLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, getDlsThemeConfiguration(),R.style.Theme_DLS_Aqua_VeryDark, null);
        uAppInterface.launch(uiLauncher, null);// pass launch input if required
        finish();
    }

    @NonNull
    private ThemeConfiguration getDlsThemeConfiguration() {
        return new ThemeConfiguration(this, ColorRange.GROUP_BLUE, NavigationColor.BRIGHT, ContentColor.VERY_DARK, AccentRange.GROUP_BLUE);
    }
}

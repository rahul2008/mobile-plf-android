package com.philips.cdp2.dscdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppLaunchInput;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class LauncherActivity extends Activity {
    private DSDemoAppuAppInterface dsDemoAppuAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
    }

    public void launch(View v) {
        UappDependencies dsDemoDeps = createDependencies();
        UappSettings dsAppSettings = createSettings();
        UiLauncher dsLauncher = createLauncher();

        dsDemoAppuAppInterface.init(dsDemoDeps, dsAppSettings);
        dsDemoAppuAppInterface.launch(dsLauncher, new DSDemoAppuAppLaunchInput());
    }

    private UappDependencies createDependencies() {
        AppInfraInterface ail = DemoApplication.getInstance().getAppInfra();
        JustInTimeTextResources jitTextRes = new JustInTimeTextResources();
        return new DSDemoAppuAppDependencies(ail, jitTextRes);
    }

    private UappSettings createSettings() {
        return new UappSettings(getApplicationContext());
    }

    private UiLauncher createLauncher() {
        return new ActivityLauncher(
                ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.VERY_LIGHT, NavigationColor.BRIGHT, AccentRange.PINK),
                R.style.Theme_DLS_Purple_VeryLight,
                null
        );
    }
}

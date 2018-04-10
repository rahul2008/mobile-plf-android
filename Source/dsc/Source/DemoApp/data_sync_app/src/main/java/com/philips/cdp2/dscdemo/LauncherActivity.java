package com.philips.cdp2.dscdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppLaunchInput;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class LauncherActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);

        LaunchFragment fragment = new LaunchFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.dscdemo_fragment_container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    // Public methods accessed by LaunchFragment
    public void launchAsActivity() {
        UiLauncher dsLauncher = new ActivityLauncher(
                ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.VERY_LIGHT, NavigationColor.BRIGHT, AccentRange.PINK),
                R.style.Theme_DLS_Purple_VeryLight,
                null
        );
        launch(dsLauncher);
    }

    public void launchAsFragment() {
        UiLauncher fragmentLauncher = new FragmentLauncher(
                this,
                R.id.dscdemo_fragment_container,
                null
        );
        launch(fragmentLauncher);
    }

    // Private helper methods
    private void launch(UiLauncher launcher) {
        UappDependencies dsDemoDeps = createDependencies();
        UappSettings dsAppSettings = createSettings();

        DSDemoAppuAppInterface dsInterface = new DSDemoAppuAppInterface();
        dsInterface.init(dsDemoDeps, dsAppSettings);
        dsInterface.launch(launcher, new DSDemoAppuAppLaunchInput());
    }

    private UappDependencies createDependencies() {
        DemoApplication app = (DemoApplication) getApplication();

        AppInfraInterface ail = app.getAppInfra();
        JustInTimeTextResources jitTextRes = new JustInTimeTextResources();
        return new DSDemoAppuAppDependencies(ail, jitTextRes);
    }

    private UappSettings createSettings() {
        return new UappSettings(getApplicationContext());
    }
}

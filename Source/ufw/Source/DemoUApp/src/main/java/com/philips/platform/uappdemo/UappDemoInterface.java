package com.philips.platform.uappdemo;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.uappdemo.screens.introscreen.LaunchActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;


public class UappDemoInterface implements UappInterface {

    private Context context;
    public static ThemeConfiguration THEME_CONFIGURATION;
    public static int DLS_THEME;
    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        getInstance().init(uappDependencies, uappSettings);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {

        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, LaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UappDemoInterface.THEME_CONFIGURATION = ((ActivityLauncher) uiLauncher).getDlsThemeConfiguration();
            UappDemoInterface.DLS_THEME = ((ActivityLauncher) uiLauncher).getUiKitTheme();
            context.startActivity(intent);
        }
    }

    protected UappDemoUiHelper getInstance(){
        return UappDemoUiHelper.getInstance();
    }
}

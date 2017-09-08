package com.philips.platform.prdemoapp;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.platform.prdemoapp.activity.MainActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.io.Serializable;


public class PRDemoAppuAppInterface implements UappInterface {

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
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PRDemoAppuAppInterface.THEME_CONFIGURATION = ((ActivityLauncher) uiLauncher).getDlsThemeConfiguration();
            PRDemoAppuAppInterface.DLS_THEME = ((ActivityLauncher) uiLauncher).getUiKitTheme();
            context.startActivity(intent);
        }
    }
}

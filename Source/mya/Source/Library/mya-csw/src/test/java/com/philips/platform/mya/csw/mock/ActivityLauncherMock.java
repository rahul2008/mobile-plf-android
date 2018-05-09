package com.philips.platform.mya.csw.mock;

import android.os.Bundle;

import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class ActivityLauncherMock extends ActivityLauncher {

    public ActivityLauncherMock(ActivityOrientation screenOrientation, ThemeConfiguration dlsThemeConfiguration, int dlsUiKitTheme, Bundle bundle) {
        super(screenOrientation, dlsThemeConfiguration, dlsUiKitTheme, bundle);
    }

}

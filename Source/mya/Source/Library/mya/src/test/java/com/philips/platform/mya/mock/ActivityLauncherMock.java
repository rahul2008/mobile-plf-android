/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import android.os.Bundle;

import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class ActivityLauncherMock extends ActivityLauncher {

    public ActivityLauncherMock(ActivityOrientation screenOrientation, ThemeConfiguration dlsThemeConfiguration, int dlsUiKitTheme, Bundle bundle) {
        super(screenOrientation, dlsThemeConfiguration, dlsUiKitTheme, bundle);
    }

}

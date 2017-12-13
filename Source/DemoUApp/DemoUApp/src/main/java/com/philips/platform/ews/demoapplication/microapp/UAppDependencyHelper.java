/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.demoapplication.microapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class UAppDependencyHelper {
    @NonNull
    private static AppInfraInterface appInfra;
    @NonNull
    private static CommCentral commCentralInstance;
    @Nullable
    private static ThemeConfiguration themeConfiguration;


    public UAppDependencyHelper(@NonNull AppInfraInterface appInfraInterface, @NonNull CommCentral commCentral) {
        appInfra = appInfraInterface;
        commCentralInstance = commCentral;
    }

    public void setThemeConfiguration(@Nullable ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }

    @Nullable
    public static ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    @NonNull
    public static AppInfraInterface getAppInfraInterface() {
        return appInfra;
    }

    @NonNull
    public static CommCentral getCommCentral() {
        return commCentralInstance;
    }
}

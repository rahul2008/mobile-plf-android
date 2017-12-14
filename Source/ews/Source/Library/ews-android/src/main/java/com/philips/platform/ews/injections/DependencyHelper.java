/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.injections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ews.configuration.ContentConfiguration;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.Map;

public class DependencyHelper {

    @NonNull private static AppInfraInterface appInfra;
    @NonNull private static CommCentral commCentralInstance;
    @NonNull private static  Map<String, String> productKeyMap;
    @Nullable private static ThemeConfiguration themeConfiguration;
    @NonNull private static ContentConfiguration contentConfiguration;

    public DependencyHelper(@NonNull AppInfraInterface appInfraInterface, @NonNull CommCentral commCentral, @NonNull  Map<String, String> prodKeyMap,
                            @NonNull ContentConfiguration configuration) {
        appInfra = appInfraInterface;
        commCentralInstance = commCentral;
        productKeyMap = prodKeyMap;
        contentConfiguration = configuration;
    }

    @NonNull
    public static ContentConfiguration getContentConfiguration() {
        return contentConfiguration;
    }

    @NonNull
    public static AppInfraInterface getAppInfraInterface() {
        return appInfra;
    }

    @NonNull
    public static CommCentral getCommCentral() {
        return commCentralInstance;
    }

    @NonNull
    public static Map<String, String> getProductKeyMap() {
        return productKeyMap;
    }

    /**
     * Check and return if appInfraInterface and productKeyMap are null or not
     * @return boolean
     */
    public static boolean areDependenciesInitialized() {
        return appInfra != null && productKeyMap != null;
    }

    public void setThemeConfiguration(@Nullable ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }

    @Nullable
    public static ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }


}

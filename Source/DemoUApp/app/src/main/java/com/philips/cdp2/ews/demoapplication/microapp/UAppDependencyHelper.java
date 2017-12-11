/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.demoapplication.microapp;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.appinfra.AppInfraInterface;

public class UAppDependencyHelper {
    @NonNull
    private static AppInfraInterface appInfra;
    @NonNull
    private static CommCentral commCentralInstance;

    public UAppDependencyHelper(@NonNull AppInfraInterface appInfraInterface, @NonNull CommCentral commCentral) {
        appInfra = appInfraInterface;
        commCentralInstance = commCentral;
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

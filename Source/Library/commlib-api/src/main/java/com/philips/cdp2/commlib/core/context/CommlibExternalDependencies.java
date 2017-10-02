/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.context;

import android.support.annotation.Nullable;

import com.philips.platform.appinfra.AppInfraInterface;

public class CommlibExternalDependencies {
    private AppInfraInterface appInfraInterface;

    public CommlibExternalDependencies(final @Nullable AppInfraInterface appInfra) {
        this.appInfraInterface = appInfra;
    }

    @Nullable
    public AppInfraInterface getAppInfra() {
        return this.appInfraInterface;
    }
}

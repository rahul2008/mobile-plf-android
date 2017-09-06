/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.bleexampledemo;

import com.facebook.stetho.Stetho;

public class DebugApplication extends BleExampleApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}

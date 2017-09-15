/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2;

import android.app.Application;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.platform.appinfra.AppInfra;

import java.util.HashMap;


public class TestApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //TODO fix the hashmap
        EWSDependencyProvider.getInstance().initDependencies(new AppInfra.Builder().build(getBaseContext()), DiscoveryManager.getInstance(),new HashMap<String, String>());
    }
}

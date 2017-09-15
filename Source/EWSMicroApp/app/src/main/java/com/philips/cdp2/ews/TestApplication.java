/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews;

import android.app.Application;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.BEApplianceFactory;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.platform.appinfra.AppInfra;

import java.util.HashMap;


public class TestApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //TODO fix the hashmap
        HashMap map = new HashMap<String, String>();
        map.put(EWSInterface.PRODUCT_NAME, Actions.Value.PRODUCT_NAME_SOMNEO);

        DICommClientWrapper.initializeDICommLibrary(getApplicationContext(),
                new BEApplianceFactory(new LanTransportContext(getApplicationContext())), null, null);

        EWSDependencyProvider.getInstance().initDependencies(new AppInfra.Builder().build(getBaseContext()), DiscoveryManager.getInstance(),map);
    }
}

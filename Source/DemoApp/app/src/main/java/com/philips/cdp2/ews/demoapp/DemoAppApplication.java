/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.demoapp;

import android.app.Application;
import android.content.Context;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.BEApplianceFactory;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

public class DemoAppApplication extends Application {
    public AppInfraInterface appInfraInterface;
    public CommCentral commCentral;

    @Override
    public void onCreate() {
        super.onCreate();
        appInfraInterface = new AppInfra.Builder().build(getApplicationContext());
        commCentral = createCommCentral(getApplicationContext(), appInfraInterface);
    }

    private CommCentral createCommCentral(Context context, AppInfraInterface appInfraInterface) {
        LanTransportContext lanTransportContext = new LanTransportContext(
                new RuntimeConfiguration(context, appInfraInterface));
        BEApplianceFactory factory = new BEApplianceFactory(lanTransportContext);
        return new CommCentral(factory, lanTransportContext);
    }
}

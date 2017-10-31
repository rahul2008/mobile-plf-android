/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest;

import android.app.Application;

import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.devicetest.appliance.BleReferenceApplianceFactory;

public class TestApplication extends Application {

    private static CommCentral commCentral;

    @Override
    public void onCreate() {
        super.onCreate();

        if (commCentral == null) {
            final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration(this, null);

            final BleTransportContext bleTransportContext = new BleTransportContext(runtimeConfiguration, false);
            final ApplianceFactory applianceFactory = new BleReferenceApplianceFactory(bleTransportContext);

            commCentral = new CommCentral(applianceFactory, bleTransportContext);
        }
    }

    public CommCentral getCommCentral() {
        return commCentral;
    }

}

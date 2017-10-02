/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest;

import android.app.Application;

import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.context.CommlibExternalDependencies;
import com.philips.cdp2.commlib.devicetest.appliance.BleReferenceApplianceFactory;

public class TestApplication extends Application {

    private CommCentral commCentral;

    @Override
    public void onCreate() {
        super.onCreate();

        final BleTransportContext bleTransportContext = new BleTransportContext(this, false);
        final ApplianceFactory applianceFactory = new BleReferenceApplianceFactory(bleTransportContext);

        final CommlibExternalDependencies dependencies = new CommlibExternalDependencies(null);

        this.commCentral = new CommCentral(dependencies, applianceFactory, bleTransportContext);
    }

    public CommCentral getCommCentral() {
        return this.commCentral;
    }

}

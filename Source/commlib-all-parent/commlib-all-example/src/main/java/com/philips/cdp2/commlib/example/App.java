/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example;

import android.app.Application;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.example.appliance.BleReferenceApplianceFactory;

public final class App extends Application {
    private CommCentral commCentral;
    private DICommApplianceFactory applianceFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        final BleTransportContext bleTransportContext = new BleTransportContext(this, false);
        this.applianceFactory = new BleReferenceApplianceFactory(bleTransportContext);
        this.commCentral = new CommCentral(this.applianceFactory, bleTransportContext);
    }

    public CommCentral getCommCentral() {
        return commCentral;
    }
}

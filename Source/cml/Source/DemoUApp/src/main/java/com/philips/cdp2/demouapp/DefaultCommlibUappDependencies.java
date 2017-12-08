/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.cloud.context.CloudTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.platform.appinfra.AppInfraInterface;

public class DefaultCommlibUappDependencies extends CommlibUappDependencies {

    private CommCentral commCentral;

    @Override
    public CommCentral getCommCentral() {
        return commCentral;
    }

    public DefaultCommlibUappDependencies(final @NonNull Context context, final @Nullable AppInfraInterface appInfraInterface) {
        final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration(context, appInfraInterface);

        final CloudController cloudController = setupCloudController(context);

        final CloudTransportContext cloudTransportContext = new CloudTransportContext(runtimeConfiguration, cloudController);
        final BleTransportContext bleTransportContext = new BleTransportContext(runtimeConfiguration, true);
        final LanTransportContext lanTransportContext = new LanTransportContext(runtimeConfiguration);

        final CommlibUappApplianceFactory applianceFactory = new CommlibUappApplianceFactory(bleTransportContext, lanTransportContext, cloudTransportContext);

        this.commCentral = new CommCentral(applianceFactory, bleTransportContext, lanTransportContext, cloudTransportContext);
    }

    @NonNull
    private CloudController setupCloudController(final @NonNull Context context) {
        final CloudController cloudController = new DefaultCloudController(context, new CommlibUappKpsConfigurationInfo());

        String ICPClientVersion = cloudController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        return cloudController;
    }
}

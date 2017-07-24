/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.cloud.context.CloudTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

public class DefaultCommlibUappDependencies extends CommlibUappDependencies {

    private CommCentral commCentral;

    @Override
    public CommCentral getCommCentral() {
        return commCentral;
    }

    public DefaultCommlibUappDependencies(Context context) {
        // Setup CommCentral
        final CloudController cloudController = setupCloudController(context);

        final LanTransportContext lanTransportContext = new LanTransportContext(context);
        final CloudTransportContext cloudTransportContext = new CloudTransportContext(cloudController);

        final CommlibUappApplianceFactory applianceFactory = new CommlibUappApplianceFactory(lanTransportContext, cloudTransportContext);

        this.commCentral = new CommCentral(applianceFactory, lanTransportContext, cloudTransportContext);

        // FIXME Remove once DiscoveryManager is removed
        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(context, applianceFactory, null, cloudController);
        }
    }

    @NonNull
    private CloudController setupCloudController(Context context) {
        final CloudController cloudController = new DefaultCloudController(context, new CommlibUappKpsConfigurationInfo());

        String ICPClientVersion = cloudController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        return cloudController;
    }
}

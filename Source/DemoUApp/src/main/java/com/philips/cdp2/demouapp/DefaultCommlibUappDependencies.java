/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
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

    public DefaultCommlibUappDependencies(final @NonNull Context context) {

        final CloudController cloudController = setupCloudController(context);
        final CloudTransportContext cloudTransportContext = new CloudTransportContext(context, cloudController);

        final LanTransportContext lanTransportContext = new LanTransportContext(context);
        final CommlibUappApplianceFactory applianceFactory = new CommlibUappApplianceFactory(lanTransportContext, cloudTransportContext);

        this.commCentral = new CommCentral(applianceFactory, lanTransportContext, cloudTransportContext);

        DiscoveryManager.createSharedInstance(context, applianceFactory);
    }

    @NonNull
    private CloudController setupCloudController(Context context) {
        final CloudController cloudController = new DefaultCloudController(context, new CommlibUappKpsConfigurationInfo());

        String ICPClientVersion = cloudController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        return cloudController;
    }
}

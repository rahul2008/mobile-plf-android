/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample;

import android.app.Application;
import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.CommCentral;
import com.philips.cdp2.commlib.context.CloudTransportContext;
import com.philips.cdp2.commlib.context.LanTransportContext;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {

    private CommCentral commCentral;

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup CommCentral
        final CloudController cloudController = setupCloudController();

        final LanTransportContext lanTransportContext = new LanTransportContext(this);
        final CloudTransportContext cloudTransportContext = new CloudTransportContext(cloudController);

        final SampleApplianceFactory applianceFactory = new SampleApplianceFactory(lanTransportContext, cloudTransportContext);
        final Set<DiscoveryStrategy> discoveryStrategies = new HashSet<DiscoveryStrategy>() {{
            add(lanTransportContext.getDiscoveryStrategy());
        }};

        this.commCentral = new CommCentral(discoveryStrategies, applianceFactory);

        // FIXME Remove once DiscoveryManager is removed
        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, applianceFactory, null, cloudController);
        }
    }

    @NonNull
    private CloudController setupCloudController() {
        final CloudController cloudController = new DefaultCloudController(this, new SampleKpsConfigurationInfo());

        String ICPClientVersion = cloudController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        return cloudController;
    }

    public CommCentral getCommCentral() {
        return commCentral;
    }
}

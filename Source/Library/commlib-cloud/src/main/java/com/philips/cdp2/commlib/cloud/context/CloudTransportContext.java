/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.cloud.context;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;

public class CloudTransportContext implements TransportContext {
    private static CloudController cloudController;

    public CloudTransportContext(@NonNull final CloudController cloudController) {
        CloudTransportContext.cloudController = cloudController;
        updateAppId();
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return null;
    }

    @Override
    public CloudCommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new CloudCommunicationStrategy(networkNode, cloudController);
    }

    public static CloudController getCloudController() {
        updateAppId();
        return cloudController;
    }

    private static void updateAppId() {
        final String appCppId = cloudController.getAppCppId();

        if (TextUtils.isEmpty(appCppId)) {
            DICommLog.w(DICommLog.CPPCONTROLLER, "Could not obtain an appId from the provided CloudController.");
        } else {
            CommCentral.setAppId(appCppId);
        }
    }
}

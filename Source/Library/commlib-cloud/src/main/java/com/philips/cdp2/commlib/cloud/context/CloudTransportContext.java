/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.cloud.context;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.util.Availability;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class CloudTransportContext implements TransportContext {
    private static CloudController cloudController;

    private final ConnectivityMonitor connectivityMonitor;

    private final Availability.AvailabilityListener availabilityListener = new Availability.AvailabilityListener() {
        @Override
        public void onAvailabilityChanged(@NonNull Availability cloud) {
            isAvailable = cloud.isAvailable();
        }
    };

    private boolean isAvailable;

    public CloudTransportContext(final @NonNull Context context, @NonNull final CloudController cloudController) {
        this.connectivityMonitor = ConnectivityMonitor.forNetworkTypes(context, TYPE_MOBILE, TYPE_WIFI);
        this.connectivityMonitor.addAvailabilityListener(availabilityListener);

        CloudTransportContext.cloudController = cloudController;
        updateAppId();
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return null;
    }

    @Override
    public CloudCommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new CloudCommunicationStrategy(networkNode, cloudController, connectivityMonitor);
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
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

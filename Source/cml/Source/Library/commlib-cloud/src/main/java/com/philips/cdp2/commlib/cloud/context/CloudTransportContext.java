/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.cloud.context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.listener.SignonListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Concrete implementation for a transport layer.
 * <p>
 * Used make a {@link CloudCommunicationStrategy}
 * for {@link Appliance}s using that transport.
 * @publicApi
 */
public class CloudTransportContext implements TransportContext<CloudTransportContext> {

    private static CloudController cloudController;

    private final ConnectivityMonitor connectivityMonitor;

    private Set<AvailabilityListener<CloudTransportContext>> availabilityListeners = new CopyOnWriteArraySet<>();

    private final AvailabilityListener<ConnectivityMonitor> availabilityListener = new AvailabilityListener<ConnectivityMonitor>() {
        @Override
        public void onAvailabilityChanged(@NonNull ConnectivityMonitor connectivityMonitor) {
            isConnected = connectivityMonitor.isAvailable();

            handleAvailabilityChanged();
        }
    };

    private final SignonListener signOnListener = new SignonListener() {
        @Override
        public void signonStatus(boolean signedOn) {
            isSignedOn = signedOn;

            updateAppId();
            handleAvailabilityChanged();
        }
    };

    private boolean isConnected;

    private boolean isSignedOn;

    private boolean isAvailable;

    /**
     * @param runtimeConfiguration RuntimeConfiguration
     * @param cloudController CloudController
     */
    public CloudTransportContext(final @NonNull RuntimeConfiguration runtimeConfiguration, @NonNull final CloudController cloudController) {
        this.connectivityMonitor = ConnectivityMonitor.forNetworkTypes(runtimeConfiguration.getContext(), TYPE_MOBILE, TYPE_WIFI);
        this.connectivityMonitor.addAvailabilityListener(availabilityListener);

        CloudTransportContext.cloudController = cloudController;
        cloudController.addSignOnListener(signOnListener);
        isSignedOn = cloudController.isSignOn();

        if (isSignedOn) {
            DICommLog.i(DICommLog.CPPCONTROLLER, "Cloud controller is signed on.");
            updateAppId();
        } else {
            DICommLog.i(DICommLog.CPPCONTROLLER, "Cloud controller is not signed on.");
            cloudController.signOnWithProvisioning();
        }
    }

    /**
     * @return null, there is no DiscoveryStrategy here.
     */
    @Override
    @Nullable
    public DiscoveryStrategy getDiscoveryStrategy() {
        return null;
    }

    @Override
    @NonNull
    public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new CloudCommunicationStrategy(networkNode, cloudController, connectivityMonitor);
    }

    /**
     * @return <code>true</code> if there is an internet connection.
     */
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public void addAvailabilityListener(@NonNull AvailabilityListener<CloudTransportContext> listener) {
        availabilityListeners.add(listener);
        listener.onAvailabilityChanged(this);
    }

    @Override
    public void removeAvailabilityListener(@NonNull AvailabilityListener<CloudTransportContext> listener) {
        availabilityListeners.remove(listener);
    }

    /**
     * @return CloudController The instance
     */
    public static CloudController getCloudController() {
        return cloudController;
    }

    private void handleAvailabilityChanged() {
        isAvailable = isConnected && isSignedOn;

        notifyAvailabilityListeners();
    }

    private void notifyAvailabilityListeners() {
        for (AvailabilityListener<CloudTransportContext> listener : availabilityListeners) {
            listener.onAvailabilityChanged(this);
        }
    }

    private void updateAppId() {
        final String appCppId = cloudController.getAppCppId();

        if (TextUtils.isEmpty(appCppId)) {
            DICommLog.w(DICommLog.CPPCONTROLLER, "Could not obtain an appId from the provided CloudController.");
        } else {
            CommCentral.getAppIdProvider().setAppId(appCppId);
        }
    }
}

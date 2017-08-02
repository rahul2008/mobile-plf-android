/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiUIPort;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Appliance.
 * <p>
 * Represents a physical appliance/peripheral/product that implements the DiComm communication protocol.
 *
 * @publicApi
 */
public abstract class Appliance {

    protected final NetworkNode networkNode;

    private final DevicePort devicePort;
    private final FirmwarePort firmwarePort;
    private final PairingPort pairingPort;
    private final WifiPort wifiPort;
    private final WifiUIPort wifiUIPort;

    protected final CommunicationStrategy communicationStrategy;

    private final Set<DICommPort> ports = new HashSet<>();

    private SubscriptionEventListener subscriptionEventListener = new SubscriptionEventListener() {

        @Override
        public void onSubscriptionEventReceived(String data) {
            DICommLog.d(DICommLog.APPLIANCE, "Notify subscription listeners - " + data);

            for (DICommPort port : getAllPorts()) {
                if (port.isResponseForThisPort(data)) {
                    port.handleResponse(data);
                }
            }
        }
    };

    public Appliance(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        this.networkNode = networkNode;
        this.communicationStrategy = communicationStrategy;
        this.communicationStrategy.addSubscriptionEventListener(subscriptionEventListener);

        devicePort = new DevicePort(this.communicationStrategy);
        firmwarePort = new FirmwarePort(this.communicationStrategy);
        pairingPort = new PairingPort(this.communicationStrategy);
        wifiPort = new WifiPort(this.communicationStrategy);
        wifiUIPort = new WifiUIPort(this.communicationStrategy);

        addPort(devicePort);
        addPort(firmwarePort);
        addPort(pairingPort);
        addPort(wifiPort);
        addPort(wifiUIPort);
    }

    /**
     * @return the device type used by CPP to identify this appliance
     */
    public abstract String getDeviceType();

    /**
     * Gets network node.
     *
     * @return the network node that is associated with this appliance
     */
    public NetworkNode getNetworkNode() {
        return networkNode;
    }

    protected void addPort(final @NonNull DICommPort port) {
        ports.add(port);
    }

    /**
     * Subscribe.
     * <p>
     * For this to work, {@link Appliance#enableCommunication()}  has to be called as well to
     * ensure this Appliance also receives subscription notifications.
     */
    public void subscribe() {
        DICommLog.i(DICommLog.APPLIANCE, "Subscribe to all ports for appliance: " + toString());

        for (DICommPort port : ports) {
            if (port.supportsSubscription()) {
                port.subscribe();
            }
        }
    }

    /**
     * Unsubscribe.
     */
    public void unsubscribe() {
        DICommLog.i(DICommLog.APPLIANCE, "Unsubscribe from all ports for appliance: " + toString());

        for (DICommPort port : ports) {
            if (port.supportsSubscription()) {
                port.unsubscribe();
            }
        }
    }

    /**
     * Stop resubscribe.
     * <p>
     * Prevents subscriptions to automatically renew after they expire.
     */
    public void stopResubscribe() {
        DICommLog.i(DICommLog.APPLIANCE, "Stop resubscribing to all ports for appliance: " + toString());

        for (DICommPort port : ports) {
            if (port.supportsSubscription()) {
                port.stopResubscribe();
            }
        }
    }

    public DevicePort getDevicePort() {
        return devicePort;
    }

    public FirmwarePort getFirmwarePort() {
        return firmwarePort;
    }

    public PairingPort getPairingPort() {
        return pairingPort;
    }

    public WifiPort getWifiPort() {
        return wifiPort;
    }

    public WifiUIPort getWifiUIPort() {
        return wifiUIPort;
    }

    Set<DICommPort> getAllPorts() {
        return ports;
    }

    public synchronized String getName() {
        return getNetworkNode().getName();
    }

    public void addListenerForAllPorts(final @NonNull DICommPortListener portListener) {
        for (DICommPort port : getAllPorts()) {
            port.addPortListener(portListener);
        }
    }

    public void removeListenerForAllPorts(final @NonNull DICommPortListener portListener) {
        for (DICommPort port : getAllPorts()) {
            port.removePortListener(portListener);
        }
    }

    /**
     * Enable listening for subscription notifications.
     */
    public void enableCommunication() {
        communicationStrategy.enableCommunication(subscriptionEventListener);
    }

    /**
     * Disable communication.
     * <p>
     * Disable listening for subscription notifications.
     */
    public void disableCommunication() {
        communicationStrategy.disableCommunication();
    }

    /**
     * Indicates that this Appliance is available for communication.
     *
     * @return true, if communication to this Appliance is possible
     */
    public boolean isAvailable() {
        return communicationStrategy.isAvailable();
    }

    @Override
    public String toString() {
        return "name: " + getName() +
                "   ip: " + getNetworkNode().getIpAddress() +
                "   eui64: " + getNetworkNode().getCppId() +
                "   bootId: " + getNetworkNode().getBootId() +
                "   paired: " + getNetworkNode().getPairedState() +
                "   homeSsid: " + getNetworkNode().getHomeSsid();
    }

    @Override
    public int hashCode() {
        return networkNode.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Appliance)) {
            return false;
        }
        return networkNode.equals(((Appliance) other).getNetworkNode());
    }
}

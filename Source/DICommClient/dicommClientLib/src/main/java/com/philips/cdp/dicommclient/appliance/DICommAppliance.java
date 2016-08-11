/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiUIPort;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.ArrayList;
import java.util.List;

public abstract class DICommAppliance {

    protected final NetworkNode mNetworkNode;

    protected final DevicePort mDevicePort;
    protected final FirmwarePort mFirmwarePort;
    protected final PairingPort mPairingPort;
    protected final WifiPort mWifiPort;
    protected final WifiUIPort mWifiUIPort;

    protected final CommunicationStrategy mCommunicationStrategy;

    private final List<DICommPort<?>> mPortList = new ArrayList<>();

    public DICommAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        mNetworkNode = networkNode;
        mCommunicationStrategy = communicationStrategy;

        mDevicePort = new DevicePort(mNetworkNode, mCommunicationStrategy);
        mFirmwarePort = new FirmwarePort(mNetworkNode, mCommunicationStrategy);
        mPairingPort = new PairingPort(mNetworkNode, mCommunicationStrategy);
        mWifiPort = new WifiPort(mNetworkNode, mCommunicationStrategy);
        mWifiUIPort = new WifiUIPort(mNetworkNode, mCommunicationStrategy);

        addPort(mDevicePort);
        addPort(mFirmwarePort);
        addPort(mPairingPort);
        addPort(mWifiPort);
        addPort(mWifiUIPort);
    }

    // TODO DIComm Refactor - remove from public interface

    /**
     * @return DeviceType used by CPP to identify this appliance
     */
    public abstract String getDeviceType();

    public NetworkNode getNetworkNode() {
        return mNetworkNode;
    }

    protected void addPort(DICommPort<?> port) {
        mPortList.add(0, port);
    }

    public void subscribe() {
        DICommLog.i(DICommLog.APPLIANCE, "Subscribe to all ports for appliance: " + this);
        for (DICommPort<?> port : mPortList) {
            if (port.supportsSubscription()) {
                port.subscribe();
            }
        }
    }

    public void unsubscribe() {
        DICommLog.i(DICommLog.APPLIANCE, "Unsubscribe to all ports for appliance: " + this);
        for (DICommPort<?> port : mPortList) {
            if (port.supportsSubscription()) {
                port.unsubscribe();
            }
        }
    }

    public void stopResubscribe() {
        DICommLog.i(DICommLog.APPLIANCE, "Stop resubscribe to all ports for appliance: " + this);
        for (DICommPort<?> port : mPortList) {
            if (port.supportsSubscription()) {
                port.stopResubscribe();
            }
        }
    }

    public DevicePort getDevicePort() {
        return mDevicePort;
    }

    public FirmwarePort getFirmwarePort() {
        return mFirmwarePort;
    }

    public PairingPort getPairingPort() {
        return mPairingPort;
    }

    public WifiPort getWifiPort() {
        return mWifiPort;
    }

    public WifiUIPort getWifiUIPort() {
        return mWifiUIPort;
    }

    protected List<DICommPort<?>> getAllPorts() {
        return mPortList;
    }

    public synchronized String getName() {
        return getNetworkNode().getName();
    }

    public void setConnectionState(ConnectionState connectionState) {
        mNetworkNode.setConnectionState(connectionState);
    }

    public void addListenerForAllPorts(DICommPortListener portListener) {
        for (DICommPort<?> port : getAllPorts()) {
            port.addPortListener(portListener);
        }
    }

    public void removeListenerForAllPorts(DICommPortListener portListener) {
        for (DICommPort<?> port : getAllPorts()) {
            port.removePortListener(portListener);
        }
    }

    public void enableSubscription() {
        mCommunicationStrategy.enableSubscription(mSubscriptionEventListener);
    }

    public void disableCommunication() {
        mCommunicationStrategy.disableCommunication();
    }

    private SubscriptionEventListener mSubscriptionEventListener = new SubscriptionEventListener() {

        @Override
        public void onSubscriptionEventReceived(String data) {
            DICommLog.d(DICommLog.APPLIANCE, "Notify subscription listeners - " + data);

            List<DICommPort<?>> portList = getAllPorts();

            for (DICommPort<?> port : portList) {
                if (port.isResponseForThisPort(data)) {
                    port.handleResponse(data);
                }
            }
        }
    };

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name: ").append(getName())//
                .append("   ip: ").append(getNetworkNode().getIpAddress())//
                .append("   eui64: ").append(getNetworkNode().getCppId())//
                .append("   bootId: ").append(getNetworkNode().getBootId())//
                .append("   paired: ").append(getNetworkNode().getPairedState())//
                .append("   connectedState: ").append(getNetworkNode().getConnectionState())//
                .append("   homeSsid: ").append(getNetworkNode().getHomeSsid());
        return builder.toString();
    }
}

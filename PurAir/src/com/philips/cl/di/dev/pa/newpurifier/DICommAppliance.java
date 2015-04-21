package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.FirmwarePort;

public abstract class DICommAppliance {

    protected final NetworkNode mNetworkNode = new NetworkNode();

    protected final FirmwarePort mFirmwarePort;

    private final DISecurity mDISecurity;

    private final List<DICommPort> portList = new ArrayList<DICommPort>();

    public DICommAppliance(CommunicationStrategy communicationStrategy, String eui64, String usn, String ipAddress, String name, long bootId,
            ConnectionState connectionState) {
        mNetworkNode.setBootId(bootId);
        mNetworkNode.setCppId(eui64);
        mNetworkNode.setIpAddress(ipAddress);
        mNetworkNode.setName(name);
        mNetworkNode.setConnectionState(connectionState);

        mDISecurity = new DISecurity(new KeyDecryptListener() {

            @Override
            public void keyDecrypt(String key, String deviceEui64) {
                if (key == null)
                    return;

                if (deviceEui64.equals(mNetworkNode.getCppId())) {
                    ALog.e(ALog.APPLIANCE, "Updated current appliance encryption key");
                    mNetworkNode.setEncryptionKey(key);
                    // TODO: DIComm Refactor, modify purifierDatabase to remove
                    // purairdevice
                    new PurifierDatabase().updatePurifierUsingUsn((AirPurifier) DICommAppliance.this);
                }
            }
        });

        mFirmwarePort = new FirmwarePort(mNetworkNode, communicationStrategy);

        addPort(mFirmwarePort);
    }

    public NetworkNode getNetworkNode() {
        return mNetworkNode;
    }

    protected void addPort(DICommPort port) {
        portList.add(port);
    }

    public void subscribe() {
        ALog.i(ALog.APPLIANCE, "Subscribe to all ports for appliance: " + this);
        for (DICommPort port : portList) {
            if (port.supportsSubscription()) {
                port.subscribe();
            }
        }
    }

    public void unsubscribe() {
        ALog.i(ALog.APPLIANCE, "Unsubscribe to all ports for appliance: " + this);
        for (DICommPort port : portList) {
            if (port.supportsSubscription()) {
                port.unsubscribe();
            }
        }
    }

    public FirmwarePort getFirmwarePort() {
        return mFirmwarePort;
    }

    public DISecurity getDISecurity() {
        return mDISecurity;
    }

    public synchronized String getName() {
        return getNetworkNode().getName();
    }

    public void setConnectionState(ConnectionState connectionState) {
        mNetworkNode.setConnectionState(connectionState);
    }

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

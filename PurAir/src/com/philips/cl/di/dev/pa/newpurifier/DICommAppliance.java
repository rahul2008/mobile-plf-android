package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.port.DICommPort;

public abstract class DICommAppliance {

    protected final NetworkNode mNetworkNode = new NetworkNode();

    private final List<DICommPort> portList = new ArrayList<DICommPort>();

    public DICommAppliance(CommunicationStrategy communicationStrategy, String eui64, String usn, String ipAddress, String name, long bootId,
            ConnectionState connectionState) {
        mNetworkNode.setBootId(bootId);
        mNetworkNode.setCppId(eui64);
        mNetworkNode.setIpAddress(ipAddress);
        mNetworkNode.setName(name);
        mNetworkNode.setConnectionState(connectionState);
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
}

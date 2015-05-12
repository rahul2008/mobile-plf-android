package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.DIPortListener;
import com.philips.cl.di.dicomm.port.DevicePort;
import com.philips.cl.di.dicomm.port.FirmwarePort;
import com.philips.cl.di.dicomm.port.PairingPort;
import com.philips.cl.di.dicomm.port.WifiPort;
import com.philips.cl.di.dicomm.port.WifiUIPort;

public abstract class DICommAppliance implements SubscriptionEventListener {

    protected final NetworkNode mNetworkNode;

    protected final DevicePort mDevicePort;
    protected final FirmwarePort mFirmwarePort;
    protected final PairingPort mPairingPort;
    protected final WifiPort mWifiPort;
    protected final WifiUIPort mWifiUIPort;

    protected final CommunicationStrategy mCommunicationStrategy;

    private final List<DICommPort<?>> mPortList = new ArrayList<DICommPort<?>>();

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
        mPortList.add(port);
    }

    public void subscribe() {
        ALog.i(ALog.APPLIANCE, "Subscribe to all ports for appliance: " + this);
        for (DICommPort<?> port : mPortList) {
            if (port.supportsSubscription()) {
                port.subscribe();
            }
        }
    }

    public void unsubscribe() {
        ALog.i(ALog.APPLIANCE, "Unsubscribe to all ports for appliance: " + this);
        for (DICommPort<?> port : mPortList) {
            if (port.supportsSubscription()) {
                port.unsubscribe();
            }
        }
    }

    public void stopResubscribe() {
        ALog.i(ALog.APPLIANCE, "Stop resubscribe to all ports for appliance: " + this);
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

    protected List<DICommPort<?>> getAllPorts(){
    	return mPortList;
    }

    public synchronized String getName() {
        return getNetworkNode().getName();
    }

    public void setConnectionState(ConnectionState connectionState) {
        mNetworkNode.setConnectionState(connectionState);
    }
    
    public void addListenerForAllPorts(DIPortListener portListener) {
		for (DICommPort<?> port : getAllPorts()) {
			port.registerPortListener(portListener);
		}
	}

	public void removeListenerForAllPorts(DIPortListener portListener) {
		for (DICommPort<?> port : getAllPorts()) {
			port.unregisterPortListener(portListener);
		}
	}
	
	public void enableSubscription() {
		mCommunicationStrategy.enableSubscription(this, mNetworkNode);
	}

	public void disableSubscription() {
		mCommunicationStrategy.disableSubscription();
	}
	
	@Override
	public void onSubscriptionEventReceived(String data) {
		ALog.d(ALog.APPLIANCE, "Notify subscription listeners - " + data);

		List<DICommPort<?>> portList = getAllPorts();

		for (DICommPort<?> port : portList) {
		    if (port.isResponseForThisPort(data)) {
		        port.handleSubscription(data);
		    }
		}
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

package com.philips.cl.di.dicomm.communication;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.UDPEventListener;
import com.philips.cl.di.dev.pa.purifier.UDPReceivingThread;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.security.DISecurity;

public class LocalSubscriptionHandler extends SubscribeHandler implements UDPEventListener{
	
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	
	public LocalSubscriptionHandler(){
	}
	
	public void registerSubscriptionListener(SubscriptionEventListener subscriptionEventListener){
		mSubscriptionEventListener = subscriptionEventListener;		
	}
	
	public void unRegisterSubscriptionListener(SubscriptionEventListener subscriptionEventListener){
		mSubscriptionEventListener = null;		
	}

	@Override
	public void enableSubscription(NetworkNode networkNode) {
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");
		mNetworkNode = networkNode;
		UDPReceivingThread.getInstance().addUDPEventListener(this) ;
		if (! UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().start();
		}
	}

	@Override
	public void disableSubscription() {
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");
		if (UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().stopUDPListener();
			// TODO: DICOMM Refactor, Only remove individual listener , do not reset. Also Do not stop thread if there is atleast single listener in the list.
			UDPReceivingThread.getInstance().reset();
		}
	}

	@Override
	public void onUDPEventReceived(String data, String fromIp) {
		if (data == null || data.isEmpty())
			return;
		if (fromIp == null || fromIp.isEmpty())
			return;
		
		if (mNetworkNode.getIpAddress() == null || !mNetworkNode.getIpAddress().equals(fromIp)) {
			ALog.d(ALog.LOCAL_SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromIp == null? "null" : fromIp) + ")");
			return;
		}

		
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "UDP event received from " + fromIp);
		
		if(mSubscriptionEventListener!=null){			
			DISecurity diSecurity = new DISecurity();
			String decryptedData = diSecurity.decryptData(data, mNetworkNode) ;
			if (decryptedData == null ) {
				ALog.d(ALog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}
			
			ALog.d(ALog.LOCAL_SUBSCRIPTION, decryptedData);
			mSubscriptionEventListener.onSubscriptionEventReceived(decryptedData);
		}
	}

}

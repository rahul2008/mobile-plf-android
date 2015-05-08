package com.philips.cl.di.dicomm.communication;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.UDPEventListener;
import com.philips.cl.di.dev.pa.purifier.UDPReceivingThread;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.security.DISecurity;

public class LocalSubscriptionHandler extends SubscribeHandler implements UDPEventListener{
	
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	private final DISecurity mDISecurity;
	
	public LocalSubscriptionHandler(DISecurity diSecurity){
		mDISecurity = diSecurity;
	}

	@Override
	public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "Enabling local subscription (start udp)");
		mNetworkNode = networkNode;
		mSubscriptionEventListener = subscriptionEventListener;
		
		UDPReceivingThread.getInstance().addUDPEventListener(this) ;
		if (! UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().start();
		}
	}

	@Override
	public void disableSubscription() {
		ALog.i(ALog.LOCAL_SUBSCRIPTION, "Disabling local subscription (stop udp)");
		mSubscriptionEventListener = null;
		UDPReceivingThread.getInstance().removeUDPEventListener(this);
		if (UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().stopUDPListener();
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
		
		if(mSubscriptionEventListener!=null && mDISecurity!=null ){			
			String decryptedData = mDISecurity.decryptData(data, mNetworkNode) ;
			if (decryptedData == null ) {
				ALog.d(ALog.LOCAL_SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}
			
			ALog.d(ALog.LOCAL_SUBSCRIPTION, decryptedData);
			if (mSubscriptionEventListener != null) {
				postSubscriptionEventOnUIThread(decryptedData, mSubscriptionEventListener);
			}
		}
	}
	
	

}

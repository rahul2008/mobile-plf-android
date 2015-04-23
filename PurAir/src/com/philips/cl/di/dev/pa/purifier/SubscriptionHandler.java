package com.philips.cl.di.dev.pa.purifier;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.security.DISecurity;

public class SubscriptionHandler implements UDPEventListener, DCSEventListener {

	private ResponseHandler mResponseHandler;
	private NetworkNode mNetworkNode;

	public SubscriptionHandler(NetworkNode networkNode, ResponseHandler responseHandler) {
		mNetworkNode = networkNode;
		mResponseHandler = responseHandler;
	}

	public void enableLocalSubscription() {
		ALog.i(ALog.SUBSCRIPTION, "Enabling local subscription (start udp)");
		UDPReceivingThread.getInstance().addUDPEventListener(this) ;
		if (! UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().start();
		}
	}

	public void disableLocalSubscription() {
		ALog.i(ALog.SUBSCRIPTION, "Disabling local subscription (stop udp)");
		if (UDPReceivingThread.getInstance().isAlive()) {
			UDPReceivingThread.getInstance().stopUDPListener();
			// TODO: DICOMM Refactor, Only remove individual listener , do not reset. Also Do not stop thread if there is atleast single listener in the list.
			UDPReceivingThread.getInstance().reset();
		}
	}

	public void enableRemoteSubscription(Context context) {
		ALog.i(ALog.SUBSCRIPTION, "Enabling remote subscription (start dcs)");
		//DI-Comm change. Moved from Constructor
		CPPController.getInstance(PurAirApplication.getAppContext()).addDCSEventListener(mNetworkNode.getCppId(), this);
		CPPController.getInstance(context).startDCSService();		
	}

	public void disableRemoteSubscription(Context context) {
		ALog.i(ALog.SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
		CPPController.getInstance(context).stopDCSService();
		//DI-Comm change. Removing the listener on Disabling remote subscroption
		CPPController.getInstance(PurAirApplication.getAppContext()).removeDCSListener(mNetworkNode.getCppId());
	}

	@Override
	public void onUDPEventReceived(String data, String fromIp) {
		if (data == null || data.isEmpty())
			return;
		if (fromIp == null || fromIp.isEmpty())
			return;
		
		if (mNetworkNode.getIpAddress() == null || !mNetworkNode.getIpAddress().equals(fromIp)) {
			ALog.d(ALog.SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromIp == null? "null" : fromIp) + ")");
			return;
		}

		ALog.i(ALog.SUBSCRIPTION, "UDP event received from " + fromIp);
		
		if (mResponseHandler != null) {			
			DISecurity diSecurity = new DISecurity();
			String decryptedData = diSecurity.decryptData(data, mNetworkNode) ;
			if (decryptedData == null ) {
				ALog.d(ALog.SUBSCRIPTION, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
				return;
			}
			
			ALog.d(ALog.SUBSCRIPTION, decryptedData);
			mResponseHandler.onSuccess(decryptedData);
		}
	}

	@Override
	public void onDCSEventReceived(String data, String fromEui64, String action) {
		ALog.i("CHECKSUB","onDCSEventReceived: "+data);
		if (data == null || data.isEmpty())
			return;

		if (fromEui64 == null || fromEui64.isEmpty())
			return;
		
		if (!mNetworkNode.getCppId().equals(fromEui64)) {
			ALog.d(ALog.SUBSCRIPTION, "Ignoring event, not from associated network node (" + (fromEui64 == null? "null" : fromEui64) + ")");
			return;
		}
		
		ALog.i(ALog.SUBSCRIPTION, "DCS event received from " + fromEui64);
		ALog.i(ALog.SUBSCRIPTION, data);
		if (mResponseHandler != null) {
			mResponseHandler.onSuccess(data);
		}
	}
}

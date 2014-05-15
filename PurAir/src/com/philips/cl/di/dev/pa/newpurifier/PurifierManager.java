package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;

/**
 * Purifier Manager is the one point contact for all UI layers to communicate
 * with the device after it has been discovered. It provides the following
 * services: unified interface for activities, limit communication to one
 * device at a time, simple interface for complex actions (e.g. pairing)
 * 
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class PurifierManager implements SubscriptionEventListener {

	private static PurifierManager instance;
	
	private PurAirDevice mCurrentPurifier = null;
	private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

	private List<AirPurifierEventListener> airPuriferEventListeners ;
	
	public static PurifierManager getInstance() {
		if (instance == null) {
			instance = new PurifierManager();
		}
		return instance;
	}
	
	private PurifierManager() {
		// Enforce Singleton
		SubscriptionManager.getInstance().setSubscriptionListener(this);
		airPuriferEventListeners = new ArrayList<AirPurifierEventListener>();
	}
	
	public synchronized void setCurrentPurifier(PurAirDevice purifier) {
		if (purifier == null) throw new RuntimeException("Cannot set null purifier");
			
		if (mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			unSubscribeFromAllEvents(mCurrentPurifier);
		}
		stopCurrentSubscription();
		
		mCurrentPurifier = purifier;
		ALog.d(ALog.PURIFIER_MANAGER, "Current purifier set to: " + purifier);
		
		startSubscription();
	}
	
	public synchronized PurAirDevice getCurrentPurifier() {
		return mCurrentPurifier;
	}

	public void subscribeToAllEvents(PurAirDevice purifier) {
		ALog.i(ALog.PURIFIER_MANAGER, "Subscribe to all events for purifier: " + purifier) ;
		SubscriptionManager.getInstance().subscribeToPurifierEvents(purifier);
		SubscriptionManager.getInstance().subscribeToFirmwareEvents(purifier);
	}

	public void unSubscribeFromAllEvents(PurAirDevice purifier) {
		ALog.i(ALog.PURIFIER_MANAGER, "UnSubscribe from all events from purifier: " + purifier) ;
		SubscriptionManager.getInstance().unSubscribeFromPurifierEvents(purifier);
		SubscriptionManager.getInstance().unSubscribeFromFirmwareEvents(purifier);
	}

	@Override
	public void onLocalEventReceived(String encryptedData, String purifierIp) {
		ALog.d(ALog.PURIFIER_MANAGER, "Local event received");
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || !purifier.getIpAddress().equals(purifierIp)) {
			ALog.d(ALog.PURIFIER_MANAGER, "Ignoring event, not from current purifier");
			return;
		}
		
		String decryptedData = new DISecurity(null).decryptData(encryptedData, purifier) ;
		if (decryptedData == null ) {
			ALog.d(ALog.PURIFIER_MANAGER, "Unable to decrypt data for current purifier: " + purifier.getIpAddress());
			return;
		}
		notifySubscriptionListeners(decryptedData) ;
	}
	
	@Override
	public void onRemoteEventReceived(String data, String purifierEui64) {
		ALog.d(ALog.PURIFIER_MANAGER, "Remote event received");
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || !purifier.getEui64().equals(purifierEui64)) {
			ALog.d(ALog.PURIFIER_MANAGER, "Ignoring event, not from current purifier");
			return;
		}
		
		notifySubscriptionListeners(data);
	}

	public void removeAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (airPuriferEventListeners) {
			airPuriferEventListeners.remove(airPurifierEventListener);
			if (airPuriferEventListeners.isEmpty()) {
				stopCurrentSubscription();
			}
		}
	}

	public void addAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (airPuriferEventListeners) {
			if (!airPuriferEventListeners.contains(airPurifierEventListener)) {
				airPuriferEventListeners.add(airPurifierEventListener);
				startSubscription();
			}
		}
	}
	
	private void setAirPortInfo(AirPortInfo airPortInfo) {
		PurAirDevice currentPurifier = getCurrentPurifier();
		if (currentPurifier == null) return;
		
		currentPurifier.setAirPortInfo(airPortInfo);
	}
	
	private void setFirmwarePortInfo(FirmwarePortInfo firmwarePortInfo) {
		PurAirDevice currentPurifier = getCurrentPurifier();
		if (currentPurifier == null) return;
		
		currentPurifier.setFirmwarePortInfo(firmwarePortInfo);
	}
	
	public void notifySubscriptionListeners(String data) {
		ALog.d(ALog.SUBSCRIPTION, "Notify subscription listeners - " + data);
		// TODO merge both JSON parsing methods.
		AirPortInfo airPortInfo = DataParser.parseAirPurifierEventData(data) ;
		AirPortInfo airPortInfoCPP = DataParser.parseAirPurifierEventDataFromCPP(data);
		FirmwarePortInfo firmwarePortInfo = DataParser.parseFirmwareEventData(data);

		synchronized (airPuriferEventListeners) {
			for (AirPurifierEventListener listener : airPuriferEventListeners) {
				if(airPortInfo != null) {
					setAirPortInfo(airPortInfo);
					listener.onAirPurifierEventReceived();
					continue;
				} 
				if(airPortInfoCPP != null) {
					setAirPortInfo(airPortInfoCPP);
					listener.onAirPurifierEventReceived();
					continue;
				} 
				if(firmwarePortInfo != null) {
					setFirmwarePortInfo(firmwarePortInfo);
					listener.onFirmwareEventReceived();
				}
			}
		}
	}

	public synchronized void startSubscription() {
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier != null) {
			mCurrentSubscriptionState = purifier.getConnectionState();
		}
		
		ALog.i(ALog.PURIFIER_MANAGER, "Start Subscription: " + mCurrentSubscriptionState);
		switch (mCurrentSubscriptionState) {
			case CONNECTED_LOCALLY: startLocalConnection(); break;
			case CONNECTED_REMOTELY: startRemoteConnection(); break;
			case DISCONNECTED: /* NOP */ break;
		}
	}

	public synchronized void stopCurrentSubscription() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop Subscription: " + mCurrentSubscriptionState);
		switch (mCurrentSubscriptionState) {
			case CONNECTED_LOCALLY: stopLocalConnection(); break;
			case CONNECTED_REMOTELY: stopRemoteConnection(); break;
			case DISCONNECTED: /* NOP */ break;
		}
	}

	private void startLocalConnection() {
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null) return;
		ALog.i(ALog.PURIFIER_MANAGER, "Start LocalConnection for purifier: " + purifier.getName() + " (" + purifier.getEui64() + ")");
		
		//Start the subscription every time it discovers the Purifier
		subscribeToAllEvents(purifier);
		SubscriptionManager.getInstance().enableLocalSubscription();
	}

	private void stopLocalConnection() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop LocalConnection") ;
		SubscriptionManager.getInstance().disableLocalSubscription();
		// Don't unsubscribe - Coming back too foreground would take longer
	}

	private void startRemoteConnection() {
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null) return;
		
		if (!purifier.isPaired()) {
			ALog.i(ALog.PURIFIER_MANAGER, "Can't start remote connection - not paired to purifier");
			return;
		}
		
		ALog.i(ALog.PURIFIER_MANAGER, "Start RemoteConnection for purifier: "  + purifier.getName() + " (" + purifier.getEui64() + ")");
		subscribeToAllEvents(purifier);
		SubscriptionManager.getInstance().enableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	private void stopRemoteConnection() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop RemoteConnection") ;
		SubscriptionManager.getInstance().disableRemoteSubscription(PurAirApplication.getAppContext());
		// Don't unsubscribe - Coming back too foreground would take longer
	}
	
	public static void setDummyPurifierManagerForTesting(PurifierManager dummyManager) {
		instance = dummyManager;
	}
}

package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;

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
	private List<AirPurifierEventListener> subscriptionEventListeners ;
	
	public static PurifierManager getInstance() {
		if (instance == null) {
			instance = new PurifierManager();
		}
		return instance;
	}
	
	private PurifierManager() {
		// Enforce Singleton
		SubscriptionManager.getInstance().setSubscriptionListener(this) ;
		subscriptionEventListeners = new ArrayList<AirPurifierEventListener>();
	}
	
	public synchronized void setCurrentPurifier(PurAirDevice purifier) {
		// TODO unsubscribe listeners from previous purifier
		mCurrentPurifier = purifier;
		// TODO subscribe listeners to new purifier
		
		ALog.d(ALog.PURIFIER_MANAGER, "Current purifier set to: " + ((purifier == null) ? "none" : purifier));
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
	public void onSubscribeEventOccurred(String encryptedData) {
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null) return;
		
		String decryptedData = new DISecurity(null).decryptData(encryptedData, purifier) ;
		if (decryptedData == null ) return;

		notifySubscriptionListeners(decryptedData) ;
	}

	public void removeAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (subscriptionEventListeners) {
			subscriptionEventListeners.remove(airPurifierEventListener) ;
		}
	}

	public void addAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (subscriptionEventListeners) {
			if (!subscriptionEventListeners.contains(airPurifierEventListener)) {
				subscriptionEventListeners.add(airPurifierEventListener) ;
			}
		}
	}
	
	public void notifySubscriptionListeners(String data) {
		ALog.d(ALog.SUBSCRIPTION, "Notify subscription listeners - " + data);
		AirPortInfo airPortInfo = DataParser.parseAirPurifierEventData(data) ;
		FirmwarePortInfo firmwarePortInfo = DataParser.parseFirmwareEventData(data);

		synchronized (subscriptionEventListeners) {
			for (AirPurifierEventListener listener : subscriptionEventListeners) {
				if(airPortInfo != null) {
					listener.onAirPurifierEventReceived(airPortInfo);
					continue;
				} 
				if(firmwarePortInfo != null) {
					listener.onFirmwareEventReceived(firmwarePortInfo);
				}
			}
		}
	}

}

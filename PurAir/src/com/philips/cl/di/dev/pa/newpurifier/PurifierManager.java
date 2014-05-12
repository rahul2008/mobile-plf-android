package com.philips.cl.di.dev.pa.newpurifier;

import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;
import com.philips.cl.di.dev.pa.util.ALog;

/**
 * Purifier Manager is the one point contact for all UI layers to communicate
 * with the device after it has been discovered. It provides the following
 * services: unified interface for activities, limit communication to one
 * device at a time, simple interface for complex actions (e.g. pairing)
 * 
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class PurifierManager {

	private static PurifierManager mInstance;
	
	private PurAirDevice mCurrentPurifier = null;
	
	public static PurifierManager getInstance() {
		if (mInstance == null) {
			mInstance = new PurifierManager();
		}
		return mInstance;
	}
	
	private PurifierManager() {
		// Enforce Singleton
	}
	
	public synchronized void setCurrentPurifier(PurAirDevice purifier) {
//		if (mCurrentPurifier != null) {
//			SubscriptionManager.getInstance().unSubscribeFromPurifierEvents(mCurrentPurifier);
//			SubscriptionManager.getInstance().unSubscribeFromFirmwareEvents(mCurrentPurifier);
//		}
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

}

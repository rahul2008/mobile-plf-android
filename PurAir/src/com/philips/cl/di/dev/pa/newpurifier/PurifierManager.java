package com.philips.cl.di.dev.pa.newpurifier;

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
		// TODO unsubscribe listeners from previous purifier
		mCurrentPurifier = purifier;
		// TODO subscribe listeners to new purifier
	}
	
	public synchronized PurAirDevice getCurrentPurifier() {
		return mCurrentPurifier;
	}
}

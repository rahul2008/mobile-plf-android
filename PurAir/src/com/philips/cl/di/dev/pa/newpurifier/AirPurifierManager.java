package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.SharedPreferences;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.port.AirPort;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.DIPortListener;
import com.philips.cl.di.dicomm.port.DIRegistration;
import com.philips.cl.di.dicomm.port.FirmwarePort;
import com.philips.cl.di.dicomm.communication.Error;

/**
 * Purifier Manager is the one point contact for all UI layers to communicate
 * with the device after it has been discovered. It provides the following
 * services: unified interface for activities, limit communication to one
 * device at a time, simple interface for complex actions (e.g. pairing)
 * 
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class AirPurifierManager implements Observer {

	private static AirPurifierManager instance;
	
	private AirPurifier mCurrentPurifier = null;
	private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

	private List<AirPurifierEventListener> airPurifierEventListeners ;

	public static enum EWS_STATE { EWS, REGISTRATION, NONE } ;
	private EWS_STATE ewsState = EWS_STATE.NONE;
	
	private int indoorViewPagerPosition;
	private DIPortListener mCurrentPurifierPortListener = new DIPortListener() {

		@Override
		public DIRegistration onPortUpdate(DICommPort<?> port) {
			if (port instanceof AirPort) {
				notifyAirPurifierEventListeners();
			} else if (port instanceof FirmwarePort) {
				notifyFirmwareEventListeners();
			}
			return DIRegistration.KEEP_REGISTERED;
		}

		@Override
		public DIRegistration onPortError(DICommPort<?> port, Error error,
				String errorData) {
			notifyAirPurifierEventListenersErrorOccurred(error);
			return DIRegistration.KEEP_REGISTERED;
		}
	};

	public static synchronized AirPurifierManager getInstance() {
		if (instance == null) {
			instance = new AirPurifierManager();
		}		
		return instance;
	}
	
	private AirPurifierManager() {
		// Enforce Singleton
		airPurifierEventListeners = new ArrayList<AirPurifierEventListener>();
	}
	
	public synchronized void setCurrentPurifier(AirPurifier purifier) {
		if (purifier == null) throw new RuntimeException("Cannot set null purifier");
		
		stopCurrentSubscription();
		if(mCurrentPurifier!=null){
			mCurrentPurifier.getNetworkNode().deleteObserver(this);
			mCurrentPurifier.removeListenerForAllPorts(mCurrentPurifierPortListener);
		}
		mCurrentPurifier = purifier;
		mCurrentPurifier.getNetworkNode().addObserver(this);
		mCurrentPurifier.addListenerForAllPorts(mCurrentPurifierPortListener);

		ALog.d(ALog.PURIFIER_MANAGER, "Current purifier set to: " + purifier);
		
		startSubscription();
		notifyPurifierChangedListeners();
	}
	
	public String getDefaultPurifierEUI64() {
		SharedPreferences prefs = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.CURR_PURAIR_PREF, Activity.MODE_PRIVATE);
		return prefs.getString(AppConstants.CURR_PURAIR_PREF_KEY, null);
	}

	public synchronized void removeCurrentPurifier() {
		if (mCurrentPurifier == null) return;
		
		if (mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			mCurrentPurifier.unsubscribe();			
		}
		mCurrentPurifier.getNetworkNode().deleteObserver(this);
		mCurrentPurifier.removeListenerForAllPorts(mCurrentPurifierPortListener);
		stopCurrentSubscription();
		
		mCurrentPurifier = null;
		ALog.d(ALog.PURIFIER_MANAGER, "Removed current purifier");
		notifyPurifierChangedListeners();
	}
	
	public synchronized AirPurifier getCurrentPurifier() {
		return mCurrentPurifier;
	}
	
	public synchronized NetworkNode getCurrentNetworkNode() {
		if(null!=mCurrentPurifier){
		    return mCurrentPurifier.getNetworkNode();
		}
		return null;
	}

	public void removeAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (airPurifierEventListeners) {
			airPurifierEventListeners.remove(airPurifierEventListener);
			if (airPurifierEventListeners.isEmpty()) {
				stopCurrentSubscription();
			}
		}
	}

	public void addAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (airPurifierEventListeners) {
			if( !airPurifierEventListeners.contains(airPurifierEventListener)) {
				airPurifierEventListeners.add(airPurifierEventListener);				
				if (airPurifierEventListeners.size() == 1) {
					// TODO optimize not to call start after adding each listener
					// TODO: DICOMM REFACTOR, need to check in case of multiple purifiers may be for powercube
					startSubscription();
				}
			}
		}
	}
	
	private void notifyPurifierChangedListeners() {
		ALog.d(ALog.PURIFIER_MANAGER, "Notify purifier changed listeners");
		
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onAirPurifierChanged();
			}
		}
	}

	public void notifyAirPurifierEventListeners() {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onAirPurifierEventReceived();
			}
		}
	}

	public void notifyFirmwareEventListeners() {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onFirmwareEventReceived();
			}
		}
	}

	public void notifyAirPurifierEventListenersErrorOccurred(Error purifierEventError) {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onErrorOccurred(purifierEventError);
			}
		}
	}

	public synchronized void startSubscription() {
	    if(airPurifierEventListeners.isEmpty()){
	        return;
	    }
	    
		AirPurifier purifier = getCurrentPurifier();
		
		if (purifier == null) return;
		
		// TODO:DICOMM REFACTOR, Need to remove after builder is introduced.
		if(mCurrentSubscriptionState == ConnectionState.CONNECTED_REMOTELY && PurAirApplication.isDemoModeEnable()){
			return;
		}
		
		purifier.subscribe();
        purifier.enableSubscription();
	}

	private synchronized void stopCurrentSubscription() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop Subscription: " + mCurrentSubscriptionState);
		AirPurifier currentPurifier = getCurrentPurifier();
		if(currentPurifier == null){
			return;
		}
		currentPurifier.disableSubscription();
		currentPurifier.stopResubscribe();
	}

	public static void setDummyPurifierManagerForTesting(AirPurifierManager dummyManager) {
		instance = dummyManager;
	}

	@Override
	public void update(Observable observable, Object data) {
		if(mCurrentPurifier == null) return;
//		switch (mCurrentPurifier.getNetworkNode().getConnectionState()) {
//		case DISCONNECTED:
//			ALog.d(ALog.PURIFIER_MANAGER, "Current purifier went offline");
//			stopCurrentSubscription();
//			break;
//		case CONNECTED_LOCALLY:
//			ALog.d(ALog.PURIFIER_MANAGER, "Current purifier connected locally");
//			stopCurrentSubscription();
//			startSubscription();
//			break;
//		case CONNECTED_REMOTELY:
//			ALog.d(ALog.PURIFIER_MANAGER, "Current purifier connected remotely");
//			stopCurrentSubscription();
//			startSubscription();
//			break;
//		}
		stopCurrentSubscription();
        startSubscription();
		notifyPurifierChangedListeners();
	}
	
	public void setEwsSate(EWS_STATE state) {
		ewsState = state;
	}
	
	public EWS_STATE getEwsState() {
		return ewsState;
	}
	
	public void setCurrentIndoorViewPagerPosition(int indoorViewPagerPosition) {
		this.indoorViewPagerPosition = indoorViewPagerPosition;
	}
	
	public int getCurrentIndoorViewPagerPosition() {
		return indoorViewPagerPosition;
	}
}

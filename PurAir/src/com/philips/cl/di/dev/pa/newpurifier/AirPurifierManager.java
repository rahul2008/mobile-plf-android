package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.appliance.CurrentApplianceManager;
import com.philips.cl.di.dicomm.appliance.DICommApplianceListener;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.port.AirPort;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.FirmwarePort;

/**
 * Purifier Manager is the one point contact for all UI layers to communicate
 * with the device after it has been discovered. It provides the following
 * services: unified interface for activities, limit communication to one
 * device at a time, simple interface for complex actions (e.g. pairing)
 * 
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class AirPurifierManager extends CurrentApplianceManager {

	private static AirPurifierManager instance;
	
	private List<AirPurifierEventListener> mAirPurifierEventListeners ;

	public static enum EWS_STATE { EWS, REGISTRATION, NONE } ;
	private EWS_STATE ewsState = EWS_STATE.NONE;
	
	private int mIndoorViewPagerPosition;
	
	private HashMap<AirPurifierEventListener, DICommApplianceListener> mApplianceEventListeners;
	
	public static synchronized AirPurifierManager getInstance() {
		if (instance == null) {
			instance = new AirPurifierManager();
		}		
		return instance;
	}
	
	private AirPurifierManager() {
		super();
		mAirPurifierEventListeners = new ArrayList<AirPurifierEventListener>();
		mApplianceEventListeners = new HashMap<AirPurifierEventListener, DICommApplianceListener>();
	}
	
	public String getDefaultPurifierEUI64() {
		SharedPreferences prefs = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.CURR_PURAIR_PREF, Activity.MODE_PRIVATE);
		return prefs.getString(AppConstants.CURR_PURAIR_PREF_KEY, null);
	}
	
	public synchronized AirPurifier getCurrentPurifier() {
		return (AirPurifier)getCurrentAppliance();
	}
	
	public void addAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (mApplianceEventListeners) {
			if( mApplianceEventListeners.containsKey(airPurifierEventListener)) {
				return;
			}
			DICommApplianceListener diCommApplianceListener = new DICommApplianceListener() {
				
			@Override
			public void onPortUpdate(DICommAppliance appliance, DICommPort<?> port) {
				if (port instanceof AirPort) {
					notifyAirPurifierEventListeners();
				} else if (port instanceof FirmwarePort) {
					notifyFirmwareEventListeners();
				}					
			}
			
			@Override
			public void onPortError(DICommAppliance appliance, DICommPort<?> port,
					Error error) {
				notifyAirPurifierEventListenersErrorOccurred(error);
			}
			
			@Override
			public void onApplianceChanged() {
				notifyPurifierChangedListeners();	
			}
		};
			mApplianceEventListeners.put(airPurifierEventListener, diCommApplianceListener);
			addApplianceListener(diCommApplianceListener);
	    }
	}
	
	public void removeAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (mApplianceEventListeners) {
			mApplianceEventListeners.remove(airPurifierEventListener);
			removeApplianceListener(mApplianceEventListeners.get(airPurifierEventListener));
		}
	}
	
	private void notifyPurifierChangedListeners() {
		ALog.d(ALog.PURIFIER_MANAGER, "Notify purifier changed listeners");
		
		synchronized (mAirPurifierEventListeners) {
			for (AirPurifierEventListener listener : mAirPurifierEventListeners) {
				listener.onAirPurifierChanged();
			}
		}
	}

	public void notifyAirPurifierEventListeners() {
		synchronized (mAirPurifierEventListeners) {
			for (AirPurifierEventListener listener : mAirPurifierEventListeners) {
				listener.onAirPurifierEventReceived();
			}
		}
	}

	public void notifyFirmwareEventListeners() {
		synchronized (mAirPurifierEventListeners) {
			for (AirPurifierEventListener listener : mAirPurifierEventListeners) {
				listener.onFirmwareEventReceived();
			}
		}
	}

	public void notifyAirPurifierEventListenersErrorOccurred(Error purifierEventError) {
		synchronized (mAirPurifierEventListeners) {
			for (AirPurifierEventListener listener : mAirPurifierEventListeners) {
				listener.onErrorOccurred(purifierEventError);
			}
		}
	}

	public static void setDummyPurifierManagerForTesting(AirPurifierManager dummyManager) {
		instance = dummyManager;
	}

	public void setEwsSate(EWS_STATE state) {
		ewsState = state;
	}
	
	public EWS_STATE getEwsState() {
		return ewsState;
	}
	
	public void setCurrentIndoorViewPagerPosition(int indoorViewPagerPosition) {
		this.mIndoorViewPagerPosition = indoorViewPagerPosition;
	}
	
	public int getCurrentIndoorViewPagerPosition() {
		return mIndoorViewPagerPosition;
	}
}

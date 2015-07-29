package com.philips.cl.di.dev.pa.newpurifier;

import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceChangedListener;
import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dicomm.port.AirPort;

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

	private static AirPurifierManager mInstance;

	public static enum EWS_STATE { EWS, REGISTRATION, NONE } ;
	private EWS_STATE ewsState = EWS_STATE.NONE;
	
	private int mIndoorViewPagerPosition;
	
	private HashMap<AirPurifierEventListener, DICommApplianceListener> mApplianceEventListeners;
	private HashMap<AirPurifierEventListener, CurrentApplianceChangedListener> mApplianceChangedListeners;

	public static synchronized AirPurifierManager getInstance() {
		if (mInstance == null) {
			mInstance = new AirPurifierManager();
		}
		return mInstance;
	}
	
	private AirPurifierManager() {
		super();
		mApplianceEventListeners = new HashMap<AirPurifierEventListener, DICommApplianceListener>();
		mApplianceChangedListeners = new HashMap<AirPurifierEventListener, CurrentApplianceChangedListener>();
	}
	
	public String getDefaultPurifierEUI64() {
		SharedPreferences prefs = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.CURR_PURAIR_PREF, Activity.MODE_PRIVATE);
		return prefs.getString(AppConstants.CURR_PURAIR_PREF_KEY, null);
	}
	
	public synchronized AirPurifier getCurrentPurifier() {
		return (AirPurifier)getCurrentAppliance();
	}

	public void addAirPurifierEventListener(final AirPurifierEventListener airPurifierEventListener) {
		synchronized (mApplianceEventListeners) {
			if( mApplianceEventListeners.containsKey(airPurifierEventListener)) {
				return;
			}
			DICommApplianceListener diCommApplianceListener = new DICommApplianceListener() {

				@Override
				public void onAppliancePortUpdate(DICommAppliance appliance, DICommPort<?> port) {
					if (port instanceof AirPort) {
						airPurifierEventListener.onAirPurifierEventReceived();
					} else if (port instanceof FirmwarePort) {
						airPurifierEventListener.onFirmwareEventReceived();
					}
				}

				@Override
				public void onAppliancePortError(DICommAppliance appliance, DICommPort<?> port,
						Error error) {
					airPurifierEventListener.onErrorOccurred(error);
				}
			};
			mApplianceEventListeners.put(airPurifierEventListener, diCommApplianceListener);
			addApplianceListener(diCommApplianceListener);

			CurrentApplianceChangedListener currentApplianceChangedListener = new CurrentApplianceChangedListener() {

				@Override
				public void onCurrentApplianceChanged() {
					airPurifierEventListener.onAirPurifierChanged();
				}
			};
			mApplianceChangedListeners.put(airPurifierEventListener, currentApplianceChangedListener);
			addCurrentApplianceChangedListener(currentApplianceChangedListener);
	    }
	}
	
	public void removeAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (mApplianceEventListeners) {
			removeApplianceListener(mApplianceEventListeners.get(airPurifierEventListener));
			removeCurrentApplianceChangedListener(mApplianceChangedListeners.get(airPurifierEventListener));
			mApplianceChangedListeners.remove(airPurifierEventListener);
			mApplianceEventListeners.remove(airPurifierEventListener);
		}
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

	public static void setDummyAirPurifierManagerForTesting(AirPurifierManager dummyManager) {
		mInstance = dummyManager;
	}
}

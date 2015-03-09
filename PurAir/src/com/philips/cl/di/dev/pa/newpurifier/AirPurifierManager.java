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
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulerListener;
import com.philips.cl.di.dev.pa.util.ALog;
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
public class AirPurifierManager implements Observer, PurifierListener {

	private static AirPurifierManager instance;
	
	private AirPurifier mCurrentPurifier = null;
	private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

	private List<AirPurifierEventListener> airPurifierEventListeners ;

	private SchedulerListener mScheduleListener ;
	
	public static enum EWS_STATE { EWS, REGISTRATION, NONE } ;
	private EWS_STATE ewsState = EWS_STATE.NONE;
	
	private int indoorViewPagerPosition;
	
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
	
	public void setSchedulerListener(SchedulerListener schedulerListener) {
		this.mScheduleListener =  schedulerListener ;
	}
	
	public synchronized void setCurrentPurifier(AirPurifier purifier) {
		if (purifier == null) throw new RuntimeException("Cannot set null purifier");
		
		if (mCurrentPurifier != null && mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			mCurrentPurifier.unSubscribeFromAllEvents();
			getCurrentPurifier().getDeviceHandler().stopDeviceThread() ;
		}
		//stopCurrentSubscription();
		if(mCurrentPurifier!=null){
			mCurrentPurifier.getNetworkNode().deleteObserver(this);
			mCurrentPurifier.setPurifierListener(null);
		}
		mCurrentPurifier = purifier;
		mCurrentPurifier.getNetworkNode().addObserver(this);
		mCurrentPurifier.setPurifierListener(this);
		
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
			mCurrentPurifier.unSubscribeFromAllEvents();			
		}
		mCurrentPurifier.getNetworkNode().deleteObserver(this);
		mCurrentPurifier.setPurifierListener(null);
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
	
	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.newpurifier.PurifierListener#notifyAirPurifierEventListeners()
	 */
	@Override
	public void notifyAirPurifierEventListeners() {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onAirPurifierEventReceived();
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.newpurifier.PurifierListener#notifyFirmwareEventListeners()
	 */
	@Override
	public void notifyFirmwareEventListeners() {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onFirmwareEventReceived();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.newpurifier.PurifierListener#notifyScheduleListenerForSingleSchedule(com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo)
	 */
	@Override
	public void notifyScheduleListenerForSingleSchedule(SchedulePortInfo schedulePortInfo){
		if(mScheduleListener!=null){
			mScheduleListener.onScheduleReceived(schedulePortInfo);
		}
	}
	
    /* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.newpurifier.PurifierListener#notifyScheduleListenerForScheduleList(java.util.List)
	 */
    @Override
	public void notifyScheduleListenerForScheduleList(List<SchedulePortInfo> schedulePortInfoList){
    	if(mScheduleListener!=null){
			mScheduleListener.onSchedulesReceived(schedulePortInfoList);
		}
	}
    
    /* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.newpurifier.PurifierListener#notifyScheduleListenerForErrorOccured(int)
	 */
    @Override
	public void notifyScheduleListenerForErrorOccured(int errorType){
    	if(mScheduleListener!=null){
			mScheduleListener.onErrorOccurred(errorType);
		}
	}

	public synchronized void startSubscription() {
		AirPurifier purifier = getCurrentPurifier();
		if (purifier != null) {
			mCurrentSubscriptionState = purifier.getNetworkNode().getConnectionState();
		}
		
		ALog.i(ALog.PURIFIER_MANAGER, "Start Subscription: " + mCurrentSubscriptionState);
		switch (mCurrentSubscriptionState) {
			case CONNECTED_LOCALLY: startLocalConnection(); break;
			case CONNECTED_REMOTELY: startRemoteConnection(); break;
			case DISCONNECTED: /* NOP */ break;
		}
	}

	private synchronized void stopCurrentSubscription() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop Subscription: " + mCurrentSubscriptionState);
		switch (mCurrentSubscriptionState) {
			case CONNECTED_LOCALLY: stopLocalConnection(); break;
			case CONNECTED_REMOTELY: stopRemoteConnection(); break;
			case DISCONNECTED: /* NOP */ break;
		}
	}

	private void startLocalConnection() {
		AirPurifier purifier = getCurrentPurifier();
		if (purifier == null) return;
		ALog.i(ALog.PURIFIER_MANAGER, "Start LocalConnection for purifier: " + purifier.getNetworkNode().getName() + " (" + purifier.getNetworkNode().getCppId() + ")");
		
		//Start the subscription every time it discovers the Purifier
		getCurrentPurifier().subscribeToAllEvents();
		getCurrentPurifier().enableLocalSubscription();
	}

	private void stopLocalConnection() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop LocalConnection") ;
		if(getCurrentPurifier() != null) {
			getCurrentPurifier().disableLocalSubscription();
		}
		// Don't unsubscribe - Coming back too foreground would take longer
	}

	private void startRemoteConnection() {
		
		if (PurAirApplication.isDemoModeEnable()) return;
		
		AirPurifier purifier = getCurrentPurifier();
		if (purifier == null) return;
		
		if (purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED) {
			ALog.i(ALog.PURIFIER_MANAGER, "Can't start remote connection - not paired to purifier");
			return;
		}
		
		ALog.i(ALog.PURIFIER_MANAGER, "Start RemoteConnection for purifier: "  + purifier.getNetworkNode().getName() + " (" + purifier.getNetworkNode().getCppId() + ")");
		getCurrentPurifier().subscribeToAllEvents();
		
		// TODO improve when/how remote subscription is enabled
		getCurrentPurifier().enableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	private void stopRemoteConnection() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop RemoteConnection - not doing anything") ;
		// Don't stop DCS, this is still needed for the discovery service!
		// Don't unsubscribe - Coming back too foreground would take longer
	}
	
	public static void setDummyPurifierManagerForTesting(AirPurifierManager dummyManager) {
		instance = dummyManager;
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.newpurifier.PurifierListener#notifyAirPurifierEventListenersErrorOccurred(com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent)
	 */
	@Override
	public void notifyAirPurifierEventListenersErrorOccurred(
			Error.PurifierEvent purifierEvent) {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onErrorOccurred(purifierEvent);
			}
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if(mCurrentPurifier == null) return;
		switch (mCurrentPurifier.getNetworkNode().getConnectionState()) {
		case DISCONNECTED:
			ALog.d(ALog.PURIFIER_MANAGER, "Current purifier went offline");
			stopCurrentSubscription();
			break;
		case CONNECTED_LOCALLY:
			ALog.d(ALog.PURIFIER_MANAGER, "Current purifier connected locally");
			stopCurrentSubscription();
			startSubscription();
			break;
		case CONNECTED_REMOTELY:
			ALog.d(ALog.PURIFIER_MANAGER, "Current purifier connected remotely");
			stopCurrentSubscription();
			startSubscription();
			break;
		}
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

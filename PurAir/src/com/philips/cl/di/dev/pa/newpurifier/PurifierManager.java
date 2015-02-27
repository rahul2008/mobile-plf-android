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
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulerHandler;
import com.philips.cl.di.dev.pa.scheduler.SchedulerListener;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
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
public class PurifierManager implements SubscriptionEventListener, KeyDecryptListener, Observer {

	private static PurifierManager instance;
	
	private DISecurity mSecurity;
	
	private PurAirDevice mCurrentPurifier = null;
	private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

	private List<AirPurifierEventListener> airPurifierEventListeners ;

	private SchedulerListener mScheduleListener ;
	
	public static enum PurifierEvent { DEVICE_CONTROL, SCHEDULER, FIRMWARE, AQI_THRESHOLD, PAIRING } ;

	public static enum EWS_STATE { EWS, REGISTRATION, NONE } ;
	private EWS_STATE ewsState = EWS_STATE.NONE;
	
	private int indoorViewPagerPosition;
	
	public static synchronized PurifierManager getInstance() {
		if (instance == null) {
			instance = new PurifierManager();
		}		
		return instance;
	}
	
	private PurifierManager() {
		// Enforce Singleton
		airPurifierEventListeners = new ArrayList<AirPurifierEventListener>();
		mSecurity = new DISecurity(this);
	}
	
	public void setSchedulerListener(SchedulerListener schedulerListener) {
		this.mScheduleListener =  schedulerListener ;
	}
	
	public synchronized void setCurrentPurifier(PurAirDevice purifier) {
		if (purifier == null) throw new RuntimeException("Cannot set null purifier");
		
		if (mCurrentPurifier != null && mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			mCurrentPurifier.unSubscribeFromAllEvents();
			getCurrentPurifier().getDeviceHandler().stopDeviceThread() ;
			mCurrentPurifier.getNetworkNode().deleteObserver(this);
		}
		//stopCurrentSubscription();
		mCurrentPurifier = purifier;
		mCurrentPurifier.getNetworkNode().addObserver(this);
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
			mCurrentPurifier.getNetworkNode().deleteObserver(this);
		}
		stopCurrentSubscription();
		
		mCurrentPurifier = null;
		ALog.d(ALog.PURIFIER_MANAGER, "Removed current purifier");
		notifyPurifierChangedListeners();
	}
	
	public synchronized PurAirDevice getCurrentPurifier() {
		return mCurrentPurifier;
	}
	
	public synchronized NetworkNode getCurrentNetworkNode() {
		if(null!=mCurrentPurifier){
		    return mCurrentPurifier.getNetworkNode();
		}
		return null;
	}

	
	@Override
	public void onLocalEventReceived(String encryptedData, String purifierIp) {
		// TODO: Refactor null check
		boolean isDeviceThreadRunning = false;
		if(getCurrentPurifier()!=null && getCurrentPurifier().getDeviceHandler().isDeviceThreadRunning()){
			isDeviceThreadRunning = true;
		}
		ALog.i("UIUX", "Check if the thread is running: " + isDeviceThreadRunning) ;
		if (isDeviceThreadRunning) return;
		
		ALog.d(ALog.PURIFIER_MANAGER, "Local event received");
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || purifier.getNetworkNode().getIpAddress() == null || !purifier.getNetworkNode().getIpAddress().equals(purifierIp)) {
			ALog.d(ALog.PURIFIER_MANAGER, "Ignoring event, not from current purifier (" + (purifierIp == null? "null" : purifierIp) + ")");
			return;
		}
		
		String decryptedData = mSecurity.decryptData(encryptedData, purifier.getNetworkNode()) ;
		if (decryptedData == null ) {
			ALog.d(ALog.PURIFIER_MANAGER, "Unable to decrypt data for current purifier: " + purifier.getNetworkNode().getIpAddress());
			return;
		}
		notifySubscriptionListeners(decryptedData) ;
	}
	
	@Override
	public void onRemoteEventReceived(String data, String purifierEui64) {
		// TODO: Refactor null check
		boolean isDeviceThreadRunning = false;
		if(getCurrentPurifier()!=null && getCurrentPurifier().getDeviceHandler().isDeviceThreadRunning()){
			isDeviceThreadRunning = true;
		}
		if (isDeviceThreadRunning) return;
		
		ALog.d(ALog.PURIFIER_MANAGER, "Remote event received");
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || !purifier.getNetworkNode().getCppId().equals(purifierEui64)) {
			ALog.d(ALog.PURIFIER_MANAGER, "Ignoring event, not from current purifier");
			return;
		}
		
		notifySubscriptionListeners(data);
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
	
	private void notifySubscriptionListeners(String data) {
		ALog.d(ALog.SUBSCRIPTION, "Notify subscription listeners - " + data);
		PurAirDevice currentPurifier = getCurrentPurifier();
		
		if(currentPurifier == null){
			return;
		}
		if(currentPurifier.getAirPort().isResponseForThisPort(data)){
			currentPurifier.getAirPort().processResponse(data);
			notifyAirPurifierEventListeners();
			return;
		}
		
		// TODO: DIComm Refactor use processresponse of schedulelist port class and make parse methods as private
		if( currentPurifier.getScheduleListPort().isResponseForThisPort(data) ){
			SchedulePortInfo schedulePortInfo = currentPurifier.getScheduleListPort().parseResponseAsSingleSchedule(data);
			if( schedulePortInfo != null ) {
				notifyScheduleListenerForSingleSchedule(schedulePortInfo);
				return ;
			}
			List<SchedulePortInfo> schedulePortInfoList = currentPurifier.getScheduleListPort().parseResponseAsScheduleList(data);
			if(  schedulePortInfoList != null ) {
				notifyScheduleListenerForScheduleList(schedulePortInfoList);
				return ;
			}
			
			if( data.contains(AppConstants.OUT_OF_MEMORY)) {
				//TODO: DICOMM Refactor: Remove dependency on schedulerhandler
				notifyScheduleListenerForErrorOccured(SchedulerHandler.MAX_SCHEDULES_REACHED);
			}
		}
		
		if(currentPurifier.getFirmwarePort().isResponseForThisPort(data)){
			currentPurifier.getFirmwarePort().processResponse(data);
            notifyFirmwareEventListeners();
			return;
		}
		
	}
	
	private void notifyAirPurifierEventListeners() {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onAirPurifierEventReceived();
			}
		}		
	}
	
	private void notifyFirmwareEventListeners() {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				listener.onFirmwareEventReceived();
			}
		}
	}
	
	private void notifyScheduleListenerForSingleSchedule(SchedulePortInfo schedulePortInfo){
		if(mScheduleListener!=null){
			mScheduleListener.onScheduleReceived(schedulePortInfo);
		}
	}
	
    private void notifyScheduleListenerForScheduleList(List<SchedulePortInfo> schedulePortInfoList){
    	if(mScheduleListener!=null){
			mScheduleListener.onSchedulesReceived(schedulePortInfoList);
		}
	}
    
    private void notifyScheduleListenerForErrorOccured(int errorType){
    	if(mScheduleListener!=null){
			mScheduleListener.onErrorOccurred(errorType);
		}
	}

	public synchronized void startSubscription() {
		PurAirDevice purifier = getCurrentPurifier();
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
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null) return;
		ALog.i(ALog.PURIFIER_MANAGER, "Start LocalConnection for purifier: " + purifier.getNetworkNode().getName() + " (" + purifier.getNetworkNode().getCppId() + ")");
		
		//Start the subscription every time it discovers the Purifier
		getCurrentPurifier().subscribeToAllEvents();
		getCurrentPurifier().enableLocalSubscription();
	}

	private void stopLocalConnection() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop LocalConnection") ;
		getCurrentPurifier().disableLocalSubscription();
		// Don't unsubscribe - Coming back too foreground would take longer
	}

	private void startRemoteConnection() {
		
		if (PurAirApplication.isDemoModeEnable()) return;
		
		PurAirDevice purifier = getCurrentPurifier();
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
	
	public static void setDummyPurifierManagerForTesting(PurifierManager dummyManager) {
		instance = dummyManager;
	}

	@Override
	public void keyDecrypt(String key, String deviceEui64) {
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null) return;
		if (key == null) return;
		
		if (deviceEui64.equals(purifier.getNetworkNode().getCppId())) {
			ALog.e(ALog.PURIFIER_MANAGER, "Updated current purifier encryption key");
			purifier.getNetworkNode().setEncryptionKey(key);
			
			new PurifierDatabase().updatePurifierUsingUsn(mCurrentPurifier);
		}
	}
	
	@Override
	public void onLocalEventLost(PurifierEvent purifierEvent) {
		switch (purifierEvent) {
		case SCHEDULER:
			if( mScheduleListener != null ) {
				//TODO: DICOMM Refactor: Remove dependency on schedulerhandler
				notifyScheduleListenerForErrorOccured(SchedulerHandler.DEFAULT_ERROR);
			}
			break;
		case DEVICE_CONTROL:
		case AQI_THRESHOLD:
			synchronized (airPurifierEventListeners) {
				for (AirPurifierEventListener listener : airPurifierEventListeners) {
					listener.onErrorOccurred(purifierEvent);
				}
			}
			break;
		case FIRMWARE:
			break;
		case PAIRING:
			break;
		default:
			break;
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

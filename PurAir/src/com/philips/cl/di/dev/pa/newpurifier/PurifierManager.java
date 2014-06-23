package com.philips.cl.di.dev.pa.newpurifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.scheduler.SchedulerListener;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
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
public class PurifierManager implements SubscriptionEventListener, KeyDecryptListener, Observer {

	private static PurifierManager instance;
	
	private DISecurity mSecurity;
	private DeviceHandler mDeviceHandler;
	
	private PurAirDevice mCurrentPurifier = null;
	private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

	private List<AirPurifierEventListener> airPurifierEventListeners ;

	private SchedulerListener scheduleListener ;
	
	public static enum PURIFIER_EVENT { DEVICE_CONTROL, SCHEDULER, FIRMWARE, AQI_THRESHOLD, PAIRING } ;
	
	public static synchronized PurifierManager getInstance() {
		if (instance == null) {
			instance = new PurifierManager();
		}		
		return instance;
	}
	
	private PurifierManager() {
		// Enforce Singleton
		SubscriptionHandler.getInstance().setSubscriptionListener(this);
		airPurifierEventListeners = new ArrayList<AirPurifierEventListener>();
		mSecurity = new DISecurity(this);
	}
	
	public void setSchedulerListener(SchedulerListener schedulerListener) {
		this.scheduleListener =  schedulerListener ;
	}
	
	public synchronized void setCurrentPurifier(PurAirDevice purifier) {
		if (purifier == null) throw new RuntimeException("Cannot set null purifier");
			
		if (mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			unSubscribeFromAllEvents(mCurrentPurifier);
			mCurrentPurifier.deleteObserver(this);
		}
		stopCurrentSubscription();
		
		mCurrentPurifier = purifier;
		mCurrentPurifier.addObserver(this);
		ALog.d(ALog.PURIFIER_MANAGER, "Current purifier set to: " + purifier);
		
		startSubscription();
		notifyPurifierChangedListeners();
	}
	
	public synchronized void removeCurrentPurifier() {
		if (mCurrentPurifier == null) return;
		
		if (mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			unSubscribeFromAllEvents(mCurrentPurifier);
			mCurrentPurifier.deleteObserver(this);
		}
		stopCurrentSubscription();
		
		mCurrentPurifier = null;
		ALog.d(ALog.PURIFIER_MANAGER, "Removed current purifier");
		notifyPurifierChangedListeners();
	}
	
	public synchronized PurAirDevice getCurrentPurifier() {
		return mCurrentPurifier;
	}

	private void subscribeToAllEvents(PurAirDevice purifier) {
		ALog.i(ALog.PURIFIER_MANAGER, "Subscribe to all events for purifier: " + purifier) ;
		SubscriptionHandler.getInstance().subscribeToPurifierEvents(purifier);
		SubscriptionHandler.getInstance().subscribeToFirmwareEvents(purifier);
	}

	private void unSubscribeFromAllEvents(PurAirDevice purifier) {
		ALog.i(ALog.PURIFIER_MANAGER, "UnSubscribe from all events from purifier: " + purifier) ;
		SubscriptionHandler.getInstance().unSubscribeFromPurifierEvents(purifier);
		SubscriptionHandler.getInstance().unSubscribeFromFirmwareEvents(purifier);
	}

	// TODO refactor into new architecture
	public void setPurifierDetails(String key, String value, PURIFIER_EVENT purifierEvent) {
		ALog.i(ALog.PURIFIER_MANAGER, "Set purifier details: " + key +" = " + value) ;
		mDeviceHandler = new DeviceHandler(this, purifierEvent) ;
		mDeviceHandler.setPurifierDetails(key, value, getCurrentPurifier());
	}
	
	@Override
	public void onLocalEventReceived(String encryptedData, String purifierIp) {
		ALog.d(ALog.PURIFIER_MANAGER, "Local event received");
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || purifier.getIpAddress() == null || !purifier.getIpAddress().equals(purifierIp)) {
			ALog.d(ALog.PURIFIER_MANAGER, "Ignoring event, not from current purifier (" + (purifierIp == null? "null" : purifierIp) + ")");
			return;
		}
		
		String decryptedData = mSecurity.decryptData(encryptedData, purifier) ;
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
		synchronized (airPurifierEventListeners) {
			airPurifierEventListeners.remove(airPurifierEventListener);
			if (airPurifierEventListeners.isEmpty()) {
				stopCurrentSubscription();
			}
		}
	}

	public void addAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		synchronized (airPurifierEventListeners) {
			if (!airPurifierEventListeners.contains(airPurifierEventListener)) {
				airPurifierEventListeners.add(airPurifierEventListener);
				if (airPurifierEventListeners.size() == 1) {
					// TODO optimize not to call start after adding each listener
					startSubscription();
				}
			}
		}
	}
	
	private void setAirPortInfo(AirPortInfo airPortInfo) {
		PurAirDevice currentPurifier = getCurrentPurifier();
		if (currentPurifier == null) return;
		if (airPortInfo == null) return;
		
		currentPurifier.setAirPortInfo(airPortInfo);
	}
	
	private void setFirmwarePortInfo(FirmwarePortInfo firmwarePortInfo) {
		PurAirDevice currentPurifier = getCurrentPurifier();
		if (currentPurifier == null) return;
		if (firmwarePortInfo == null) return;
		
		currentPurifier.setFirmwarePortInfo(firmwarePortInfo);
	}
	
	private void notifyPurifierChangedListeners() {
		ALog.d(ALog.PURIFIER_MANAGER, "Notify purifier changed listeners");
		
		ALog.d(ALog.TEMP, "Started notifying listeners");
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				ALog.d(ALog.TEMP, "Listener: " + listener.getClass().getSimpleName());
				listener.onAirPurifierChanged();
			}
		}
		ALog.d(ALog.TEMP, "Stopped notifying listeners");
	}
	
	private void notifySubscriptionListeners(String data) {
		ALog.d(ALog.SUBSCRIPTION, "Notify subscription listeners - " + data);
		AirPortInfo airPortInfo = DataParser.parseAirPurifierEventData(data) ;
		if( airPortInfo != null) {
			notifyAirPurifierEventListeners(airPortInfo) ;
			return ;
		}
		FirmwarePortInfo firmwarePortInfo = DataParser.parseFirmwareEventData(data);
		if( firmwarePortInfo != null ) {
			notifyFirmwareEventListeners(firmwarePortInfo) ;
			return ;
		}
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(data) ;
		List<SchedulePortInfo> schedulerPortInfoList = DataParser.parseSchedulerDto(data) ;
		
		if( scheduleListener != null) {
			if( schedulePortInfo != null ) {
				scheduleListener.onScheduleReceived(schedulePortInfo) ;
				return ;
			}
			else if(  schedulerPortInfoList != null ) {
				scheduleListener.onSchedulesReceived(schedulerPortInfoList) ;
				return ;
			}
		}	
	}
	
	private void notifyAirPurifierEventListeners(AirPortInfo airPortInfo) {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
				if(airPortInfo != null) {
					setAirPortInfo(airPortInfo);
					listener.onAirPurifierEventReceived();
				}
			}
		}		
	}
	
	private void notifyFirmwareEventListeners(FirmwarePortInfo firmwarePortInfo) {
		synchronized (airPurifierEventListeners) {
			for (AirPurifierEventListener listener : airPurifierEventListeners) {
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
		ALog.i(ALog.PURIFIER_MANAGER, "Start LocalConnection for purifier: " + purifier.getName() + " (" + purifier.getEui64() + ")");
		
		//Start the subscription every time it discovers the Purifier
		subscribeToAllEvents(purifier);
		SubscriptionHandler.getInstance().enableLocalSubscription();
	}

	private void stopLocalConnection() {
		ALog.i(ALog.PURIFIER_MANAGER, "Stop LocalConnection") ;
		SubscriptionHandler.getInstance().disableLocalSubscription();
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
		SubscriptionHandler.getInstance().enableRemoteSubscription(PurAirApplication.getAppContext());
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
		
		if (deviceEui64.equals(purifier.getEui64())) {
			ALog.e(ALog.PURIFIER_MANAGER, "Updated current purifier encryption key");
			purifier.setEncryptionKey(key);
			
			PurifierDatabase db = new PurifierDatabase();
			db.insertPurAirDevice(mCurrentPurifier);
			db.closeDb();
		}
	}
	
	public void sendScheduleDetailsToPurifier(String data, PurAirDevice purAirDevice, SCHEDULE_TYPE scheduleType,int scheduleNumber) {
		mDeviceHandler = new DeviceHandler(this,PURIFIER_EVENT.SCHEDULER) ;
		mDeviceHandler.setScheduleDetails(data, purAirDevice,scheduleType,scheduleNumber);
	}

	@Override
	public void onLocalEventLost(PURIFIER_EVENT purifierEvent) {
		switch (purifierEvent) {
		case SCHEDULER:
			if( scheduleListener != null ) {
				scheduleListener.onErrorOccurred() ;
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
		switch (mCurrentPurifier.getConnectionState()) {
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
}

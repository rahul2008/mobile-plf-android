package com.philips.cl.di.dicomm.appliance;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.DIPortListener;
import com.philips.cl.di.dicomm.port.DIRegistration;


public class CurrentApplianceManager implements Observer {

	private static CurrentApplianceManager mCurrentApplianceManager;
	
	private DICommAppliance mDICommAppliance = null;
	private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

	private List<DICommApplianceListener> mApplianceListenersList ;

	
	private DIPortListener mDICommAppliancePortListener = new DIPortListener() {
		@Override
		public DIRegistration onPortUpdate(DICommPort<?> port) {
			notifyApplianceListenersOnSuccess(port);
			return DIRegistration.KEEP_REGISTERED;
		}

		@Override
		public DIRegistration onPortError(DICommPort<?> port, Error error,
				String errorData) {
			notifyApplianceListenersOnErrorOccurred(port, error);
			return DIRegistration.KEEP_REGISTERED;
		}
	};

	public static synchronized CurrentApplianceManager getInstance() {
		if (mCurrentApplianceManager == null) {
			mCurrentApplianceManager = new CurrentApplianceManager();
		}		
		return mCurrentApplianceManager;
	}
	
	protected CurrentApplianceManager() {
		mApplianceListenersList = new ArrayList<DICommApplianceListener>();
	}
	
	public synchronized void setCurrentAppliance(DICommAppliance diCommAppliance) {
		if (diCommAppliance == null) throw new RuntimeException("Cannot set null appliance");
		
		stopCurrentSubscription();
		if(mDICommAppliance!=null){
			mDICommAppliance.getNetworkNode().deleteObserver(this);
			mDICommAppliance.removeListenerForAllPorts(mDICommAppliancePortListener);
		}
		mDICommAppliance = diCommAppliance;
		mDICommAppliance.getNetworkNode().addObserver(this);
		mDICommAppliance.addListenerForAllPorts(mDICommAppliancePortListener);

		ALog.d(ALog.APPLIANCE_MANAGER, "Current appliance set to: " + diCommAppliance);
		
		startSubscription();
		notifyApplianceChanged();
	}
	
	public synchronized void removeCurrentAppliance() {
		if (mDICommAppliance == null) return;
		
		if (mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
			mDICommAppliance.unsubscribe();			
		}
		mDICommAppliance.getNetworkNode().deleteObserver(this);
		mDICommAppliance.removeListenerForAllPorts(mDICommAppliancePortListener);
		stopCurrentSubscription();
		
		mDICommAppliance = null;
		ALog.d(ALog.APPLIANCE_MANAGER, "Removed current appliance");
		notifyApplianceChanged();
	}
	
	public synchronized DICommAppliance getCurrentAppliance() {
		return mDICommAppliance;
	}
	
	public synchronized NetworkNode getCurrentNetworkNode() {
		if(null!=mDICommAppliance){
		    return mDICommAppliance.getNetworkNode();
		}
		return null;
	}

	protected void addApplianceListener(DICommApplianceListener applianceListener) {
		synchronized (mApplianceListenersList) {
			if( !mApplianceListenersList.contains(applianceListener)) {
				mApplianceListenersList.add(applianceListener);				
				if (mApplianceListenersList.size() == 1) {
					// TODO optimize not to call start after adding each listener
					// TODO: DICOMM REFACTOR, need to check in case of multiple appliances may be for powercube
					startSubscription();
				}
			}
		}
	}
	
	public void removeApplianceListener(DICommApplianceListener applianceListener) {
		synchronized (mApplianceListenersList) {
			mApplianceListenersList.remove(applianceListener);
			if (mApplianceListenersList.isEmpty()) {
				stopCurrentSubscription();
			}
		}
	}
	
	private void notifyApplianceListenersOnSuccess(DICommPort<?> port) {
		ALog.d(ALog.APPLIANCE_MANAGER, "Notify appliance changed listeners");
		
		synchronized (mApplianceListenersList) {
			for (DICommApplianceListener listener : mApplianceListenersList) {
				    listener.onPortUpdate(mDICommAppliance, port);
			}
		}
	}
	
	private void notifyApplianceListenersOnErrorOccurred(DICommPort<?> port,Error error) {
		synchronized (mApplianceListenersList) {
			for (DICommApplianceListener listener : mApplianceListenersList) {
				listener.onPortError(mDICommAppliance, port, error);
			}
		}
	}
	
	private void notifyApplianceChanged() {
		ALog.d(ALog.APPLIANCE_MANAGER, "Notify appliance changed");
		
		synchronized (mApplianceListenersList) {
			for (DICommApplianceListener listener : mApplianceListenersList) {
				    listener.onApplianceChanged();
			}
		}
	}


	public synchronized void startSubscription() {
	    if(mApplianceListenersList.isEmpty()){
	        return;
	    }
	    
		DICommAppliance appliance = getCurrentAppliance();
		
		if (appliance == null) return;
		
		// TODO:DICOMM REFACTOR, Need to remove after builder is introduced.
		if(mCurrentSubscriptionState == ConnectionState.CONNECTED_REMOTELY && PurAirApplication.isDemoModeEnable()){
			return;
		}
		
		appliance.subscribe();
		appliance.enableSubscription();
	}

	private synchronized void stopCurrentSubscription() {
		ALog.i(ALog.APPLIANCE_MANAGER, "Stop Subscription: " + mCurrentSubscriptionState);
		DICommAppliance diCommAppliance = getCurrentAppliance();
		if(diCommAppliance == null){
			return;
		}
		diCommAppliance.disableSubscription();
		diCommAppliance.stopResubscribe();
	}

	@Override
	public void update(Observable observable, Object data) {
		if(mDICommAppliance == null) return;
		stopCurrentSubscription();
        startSubscription();
		notifyApplianceChanged();
	}
	
}

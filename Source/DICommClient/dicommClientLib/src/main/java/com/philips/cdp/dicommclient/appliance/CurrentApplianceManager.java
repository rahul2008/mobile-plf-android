/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CurrentApplianceManager implements PropertyChangeListener {

    private static CurrentApplianceManager mInstance;

    private Appliance mAppliance = null;
    private ConnectionState mCurrentSubscriptionState = ConnectionState.DISCONNECTED;

    private final List<DICommApplianceListener> mApplianceListenersList;
    private final List<CurrentApplianceChangedListener> mCurrentApplianceChangedListenerList;

    private DICommPortListener mDICommAppliancePortListener = new DICommPortListener<DICommPort<?>>() {

        @Override
        public void onPortUpdate(DICommPort<?> port) {
            notifyApplianceListenersOnSuccess(port);
        }

        @Override
        public void onPortError(DICommPort<?> port, Error error,
                                String errorData) {
            notifyApplianceListenersOnErrorOccurred(port, error);
        }
    };

    public static synchronized CurrentApplianceManager getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentApplianceManager();
        }
        return mInstance;
    }

    protected CurrentApplianceManager() {
        mApplianceListenersList = new ArrayList<>();
        mCurrentApplianceChangedListenerList = new ArrayList<>();
    }

    public synchronized void setCurrentAppliance(Appliance appliance) {
        if (appliance == null) throw new RuntimeException("Cannot set null appliance");

        stopCurrentSubscription();
        if (mAppliance != null) {
            mAppliance.getNetworkNode().removePropertyChangeListener(this);
            mAppliance.removeListenerForAllPorts(mDICommAppliancePortListener);
        }
        mAppliance = appliance;
        mAppliance.getNetworkNode().addPropertyChangeListener(this);
        mAppliance.addListenerForAllPorts(mDICommAppliancePortListener);

        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Current appliance set to: " + appliance);

        startSubscription();
        notifyApplianceChanged();
    }

    public synchronized void removeCurrentAppliance() {
        if (mAppliance == null) return;

        if (mCurrentSubscriptionState != ConnectionState.DISCONNECTED) {
            mAppliance.unsubscribe();
        }
        mAppliance.getNetworkNode().removePropertyChangeListener(this);
        mAppliance.removeListenerForAllPorts(mDICommAppliancePortListener);
        stopCurrentSubscription();

        mAppliance = null;
        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Removed current appliance");
        notifyApplianceChanged();
    }

    public synchronized Appliance getCurrentAppliance() {
        return mAppliance;
    }

    public void addApplianceListener(DICommApplianceListener applianceListener) {
        synchronized (mApplianceListenersList) {
            if (!mApplianceListenersList.contains(applianceListener)) {
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

    public void addCurrentApplianceChangedListener(CurrentApplianceChangedListener currentApplianceChangedListener) {
        synchronized (mCurrentApplianceChangedListenerList) {
            if (!mCurrentApplianceChangedListenerList.contains(currentApplianceChangedListener)) {
                mCurrentApplianceChangedListenerList.add(currentApplianceChangedListener);
            }
        }
    }

    public void removeCurrentApplianceChangedListener(CurrentApplianceChangedListener currentApplianceChangedListener) {
        synchronized (mCurrentApplianceChangedListenerList) {
            mCurrentApplianceChangedListenerList.remove(currentApplianceChangedListener);
        }
    }

    private void notifyApplianceListenersOnSuccess(DICommPort<?> port) {
        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Notify appliance changed listeners");

        synchronized (mApplianceListenersList) {
            for (DICommApplianceListener listener : mApplianceListenersList) {
                listener.onAppliancePortUpdate(mAppliance, port);
            }
        }
    }

    private void notifyApplianceListenersOnErrorOccurred(DICommPort<?> port, Error error) {
        synchronized (mApplianceListenersList) {
            for (DICommApplianceListener listener : mApplianceListenersList) {
                listener.onAppliancePortError(mAppliance, port, error);
            }
        }
    }

    private void notifyApplianceChanged() {
        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Notify appliance changed");

        synchronized (mCurrentApplianceChangedListenerList) {
            for (CurrentApplianceChangedListener listener : mCurrentApplianceChangedListenerList) {
                listener.onCurrentApplianceChanged();
            }
        }
    }

    public synchronized void startSubscription() {
        if (mApplianceListenersList.isEmpty()) {
            return;
        }

        Appliance appliance = getCurrentAppliance();

        if (appliance == null) {
            return;
        }

        if (!appliance.getNetworkNode().getConnectionState().equals(ConnectionState.DISCONNECTED)) {
            appliance.subscribe();
            appliance.enableCommunication();
        }
    }

    private synchronized void stopCurrentSubscription() {
        DICommLog.i(DICommLog.APPLIANCE_MANAGER, "Stop Subscription: " + mCurrentSubscriptionState);
        Appliance appliance = getCurrentAppliance();
        if (appliance == null) {
            return;
        }
        appliance.disableCommunication();
        appliance.stopResubscribe();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (mAppliance == null) return;
        stopCurrentSubscription();
        startSubscription();
        notifyApplianceChanged();
    }

    public static void setDummyCurrentApplianceManagerForTesting(CurrentApplianceManager dummyManager) {
        mInstance = dummyManager;
    }
}

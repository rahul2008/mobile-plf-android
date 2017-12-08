/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceChangedListener;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The CurrentApplianceManager performs automatic subscription handling for the appliance that is currently used.
 *
 * @publicApi
 */
public class CurrentApplianceManager implements PropertyChangeListener {

    private static CurrentApplianceManager instance;

    private Appliance appliance = null;

    private final Set<DICommApplianceListener> applianceListeners = new CopyOnWriteArraySet<>();
    private final Set<CurrentApplianceChangedListener> currentApplianceChangedListeners = new CopyOnWriteArraySet<>();

    private final DICommPortListener diCommAppliancePortListener = new DICommPortListener<DICommPort>() {

        @Override
        public void onPortUpdate(DICommPort port) {
            notifyApplianceListenersOnSuccess(port);
        }

        @Override
        public void onPortError(DICommPort port, Error error, String errorData) {
            notifyApplianceListenersOnErrorOccurred(port, error);
        }
    };

    public static synchronized CurrentApplianceManager getInstance() {
        if (instance == null) {
            instance = new CurrentApplianceManager();
        }
        return instance;
    }

    public synchronized void setCurrentAppliance(final @NonNull Appliance appliance) {
        if (appliance.equals(this.appliance)) {
            return;
        }
        stopCurrentSubscription();

        if (this.appliance != null) {
            this.appliance.getNetworkNode().removePropertyChangeListener(this);
            this.appliance.removeListenerForAllPorts(diCommAppliancePortListener);
        }
        this.appliance = appliance;
        this.appliance.getNetworkNode().addPropertyChangeListener(this);
        this.appliance.addListenerForAllPorts(diCommAppliancePortListener);

        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Current appliance set to: " + appliance.toString());

        startSubscription();
        notifyApplianceChanged();
    }

    public synchronized void removeCurrentAppliance() {
        if (appliance == null) return;

        appliance.unsubscribe();
        appliance.getNetworkNode().removePropertyChangeListener(this);
        appliance.removeListenerForAllPorts(diCommAppliancePortListener);
        stopCurrentSubscription();

        appliance = null;
        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Removed current appliance");
        notifyApplianceChanged();
    }

    @Nullable
    public final Appliance getCurrentAppliance() {
        return this.appliance;
    }

    public void addApplianceListener(final @NonNull DICommApplianceListener applianceListener) {
        applianceListeners.add(applianceListener);

        if (applianceListeners.size() == 1) {
            startSubscription();
        }
    }

    public void removeApplianceListener(final @NonNull DICommApplianceListener applianceListener) {
        applianceListeners.remove(applianceListener);

        if (applianceListeners.isEmpty()) {
            stopCurrentSubscription();
        }
    }

    public void addCurrentApplianceChangedListener(final @NonNull CurrentApplianceChangedListener currentApplianceChangedListener) {
        currentApplianceChangedListeners.add(currentApplianceChangedListener);
    }

    public void removeCurrentApplianceChangedListener(final @NonNull CurrentApplianceChangedListener currentApplianceChangedListener) {
        currentApplianceChangedListeners.remove(currentApplianceChangedListener);
    }

    private void notifyApplianceListenersOnSuccess(final @NonNull DICommPort port) {
        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Notify appliance changed listeners");

        for (DICommApplianceListener listener : applianceListeners) {
            listener.onAppliancePortUpdate(appliance, port);
        }
    }

    private void notifyApplianceListenersOnErrorOccurred(final @NonNull DICommPort port, final @NonNull Error error) {
        for (DICommApplianceListener listener : applianceListeners) {
            listener.onAppliancePortError(appliance, port, error);
        }
    }

    private void notifyApplianceChanged() {
        DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Notify appliance changed");

        for (CurrentApplianceChangedListener listener : currentApplianceChangedListeners) {
            listener.onCurrentApplianceChanged();
        }
    }

    private synchronized void startSubscription() {
        if (applianceListeners.isEmpty()) {
            return;
        }
        final Appliance appliance = getCurrentAppliance();

        if (appliance == null) {
            return;
        }

        if (appliance.isAvailable()) {
            appliance.subscribe();
        }
    }

    private synchronized void stopCurrentSubscription() {
        DICommLog.i(DICommLog.APPLIANCE_MANAGER, "Stop current subscription.");

        final Appliance appliance = getCurrentAppliance();
        if (appliance == null) {
            return;
        }
        appliance.stopResubscribe();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (appliance == null) {
            return;
        }
        stopCurrentSubscription();
        startSubscription();

        notifyApplianceChanged();
    }

    @Deprecated
    static void setDummyCurrentApplianceManagerForTesting(final CurrentApplianceManager dummyManager) {
        instance = dummyManager;
    }
}

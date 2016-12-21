/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type ApplianceManager.
 * <p>
 * Acts as a facade between an application and multiple {@link DiscoveryStrategy}s.
 * Any observer subscribed to an instance of this type is notified of events such as
 * when an appliance is found or updated, or whenever an error occurs while performing discovery.
 * <p>
 * The application should subscribe to notifications using the {@link ApplianceManagerListener} interface.
 * It's also possible to just obtain the set of available appliances using {@link #getAvailableAppliances()}
 */
public class ApplianceManager implements DiscoveryStrategy.DiscoveryListener {

    /**
     * The interface ApplianceManagerListener.
     */
    public interface ApplianceManagerListener {
        /**
         * On appliance found.
         *
         * @param <A>            the specific
         * @param foundAppliance the found appliance
         */
        <A extends DICommAppliance> void onApplianceFound(@NonNull A foundAppliance);
    }

    private final DICommApplianceFactory applianceFactory;

    private final Set<ApplianceManagerListener> applianceManagerListeners = new CopyOnWriteArraySet<>();
    private Set<DICommAppliance> availableAppliances = new CopyOnWriteArraySet<>();

    /**
     * Instantiates a new ApplianceManager.
     *
     * @param applianceFactory the appliance factory
     */
    public ApplianceManager(@NonNull DICommApplianceFactory applianceFactory) {

        if (applianceFactory == null) {
            throw new IllegalArgumentException("This class needs to be constructed with a non-null appliance factory.");
        }
        this.applianceFactory = applianceFactory;

        loadAppliancesFromPersistentStorage();
    }

    /**
     * Gets available appliances.
     *
     * @return The currently available appliances
     */
    public Set<? extends DICommAppliance> getAvailableAppliances() {
        return availableAppliances;
    }

    /**
     * Store an appliance.
     *
     * @param <A>       the appliance type parameter
     * @param appliance the appliance
     */
    public <A extends DICommAppliance> void storeAppliance(@NonNull A appliance) {
        // TODO store, notify availableAppliances
    }

    /**
     * Add a listener.
     *
     * @param applianceManagerListener the listener
     * @return true, if the listener didn't exist yet and was therefore added
     */
    public boolean addApplianceManagerListener(@NonNull ApplianceManagerListener applianceManagerListener) {
        return applianceManagerListeners.add(applianceManagerListener);
    }

    /**
     * Remove a listener.
     *
     * @param applianceManagerListener the listener
     * @return true, if the listener was present and therefore removed
     */
    public boolean removeApplianceManagerListener(@NonNull ApplianceManagerListener applianceManagerListener) {
        return applianceManagerListeners.remove(applianceManagerListener);
    }

    @Override
    public void onDiscoveryStarted() {
        // TODO notify observers (?)
    }

    @Override
    public void onNetworkNodeDiscovered(NetworkNode networkNode) {
        final DICommAppliance appliance = createAppliance(networkNode);

        if (appliance == null) {
            return;
        }
        availableAppliances.add(appliance);

        // TODO Perform cast to actual subclass of DICommAppliance using its device type, or the discovery strategy that found it (if possible?)
        notifyApplianceFound(appliance);
    }

    @Override
    public void onNetworkNodeLost(NetworkNode networkNode) {
        // TODO find Appliance and remove from availableAppliances, notify

        final DICommAppliance appliance = createAppliance(networkNode);
        availableAppliances.remove(appliance);

        // FIXME Remove appliance from availableAppliances using the NetworkNode's cppId.
    }

    @Override
    public void onNetworkNodeUpdated(NetworkNode networkNode) {
        // TODO find Appliance and update in availableAppliances, notify

        for (DICommAppliance appliance : availableAppliances) {
            if (networkNode.getCppId().equals(appliance.getNetworkNode().getCppId())) {
                // TODO Perform merge/update
                break;
            }
        }
    }

    @Override
    public void onDiscoveryStopped() {
        // TODO notify observers (?)
    }

    private void loadAppliancesFromPersistentStorage() {
        // TODO implement
    }

    private DICommAppliance createAppliance(NetworkNode networkNode) {
        if (this.applianceFactory.canCreateApplianceForNode(networkNode)) {
            return (DICommAppliance) applianceFactory.createApplianceForNode(networkNode);
        }
        return null;
    }

    private <A extends DICommAppliance> void notifyApplianceFound(@NonNull A appliance) {
        for (ApplianceManagerListener listener : applianceManagerListeners) {
            listener.onApplianceFound(appliance);
        }
    }
}

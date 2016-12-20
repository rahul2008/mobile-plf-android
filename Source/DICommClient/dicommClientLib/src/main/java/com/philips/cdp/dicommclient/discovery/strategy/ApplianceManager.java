/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Collection;
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
public class ApplianceManager {

    /**
     * The interface ApplianceManagerListener.
     */
    interface ApplianceManagerListener {
        /**
         * On discovery failure.
         *
         * @param reason the reason
         */
        void onDiscoveryFailure(@NonNull Throwable reason);

        /**
         * On appliance found.
         *
         * @param <A>       the specific
         * @param appliance the appliance
         */
        <A extends DICommAppliance> void onApplianceFound(@NonNull A appliance);

        /**
         * On appliance updated.
         *
         * @param <A>       the type parameter
         * @param appliance the appliance
         */
        <A extends DICommAppliance> void onApplianceUpdated(@NonNull A appliance);
    }

    private final Context context;
    private final Collection<String> deviceTypes;
    private Set<DiscoveryStrategy> discoveryStrategies = new CopyOnWriteArraySet<>();
    private Set<DICommApplianceFactory> applianceFactories = new CopyOnWriteArraySet<>();

    private final Set<ApplianceManagerListener> applianceManagerListeners = new CopyOnWriteArraySet<>();
    private Set<DICommAppliance> availableAppliances = new CopyOnWriteArraySet<>();

    private final DiscoveryStrategy.DiscoveryListener discoveryListener = new DiscoveryStrategy.DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {
            // TODO notify observers (?)
        }

        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            final DICommAppliance appliance = createOrMergeAppliance(networkNode);

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

            final DICommAppliance appliance = createOrMergeAppliance(networkNode);
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
    };

    /**
     * Instantiates a new ApplianceManager.
     *
     * @param context             the context
     * @param deviceTypes         the device types to support as defined via {@link DICommAppliance#getDeviceType()}
     * @param discoveryStrategies the discovery strategies
     * @param applianceFactories  the appliance factories
     */
    public ApplianceManager(@NonNull Context context, @NonNull Collection<String> deviceTypes, @NonNull Set<DiscoveryStrategy> discoveryStrategies, @NonNull Set<DICommApplianceFactory> applianceFactories) {
        this.context = context;
        this.deviceTypes = deviceTypes;

        if (discoveryStrategies.isEmpty()) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one discovery strategy.");
        }
        this.discoveryStrategies.addAll(discoveryStrategies);

        if (applianceFactories.isEmpty()) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one appliance factory.");
        }
        this.applianceFactories.addAll(applianceFactories);
    }

    /**
     * Start.
     * <p>
     * If for some reason any of the provided {@link DiscoveryStrategy}s fails during start,
     * the subscribed {@link ApplianceManagerListener}s are notified of this
     * via {@link ApplianceManagerListener#onDiscoveryFailure(Throwable)}
     */
    public void startDiscovery() {
        loadAppliancesFromPersistentStorage();

        for (DiscoveryStrategy strategy : discoveryStrategies) {
            strategy.addDiscoveryListener(discoveryListener);

            try {
                strategy.start(context, deviceTypes);
            } catch (MissingPermissionException e) {
                notifyDiscoveryFailure(e);
            }
        }
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
    public boolean removeApplianceListenerListener(@NonNull ApplianceManagerListener applianceManagerListener) {
        return applianceManagerListeners.remove(applianceManagerListener);
    }

    private void loadAppliancesFromPersistentStorage() {
        // TODO implement
    }

    private DICommAppliance createOrMergeAppliance(NetworkNode networkNode) {
        for (DICommApplianceFactory factory : applianceFactories) {
            if (factory.canCreateApplianceForNode(networkNode)) {
                DICommAppliance appliance = (DICommAppliance) factory.createApplianceForNode(networkNode);

                if (appliance == null) {
                    continue;
                }
                // TODO perform merge with existing appliance, if any

                return appliance;
            }
        }
        return null;
    }

    private void notifyDiscoveryFailure(@NonNull Throwable reason) {
        for (ApplianceManagerListener listener : applianceManagerListeners) {
            listener.onDiscoveryFailure(reason);
        }
    }

    private <A extends DICommAppliance> void notifyApplianceFound(@NonNull A appliance) {
        for (ApplianceManagerListener listener : applianceManagerListeners) {
            listener.onApplianceFound(appliance);
        }
    }

    private <A extends DICommAppliance> void notifyApplianceUpdated(@NonNull A appliance) {
        for (ApplianceManagerListener listener : applianceManagerListeners) {
            listener.onApplianceUpdated(appliance);
        }
    }
}

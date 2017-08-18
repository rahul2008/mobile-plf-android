/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.createHandler;

/**
 * The type ApplianceManager.
 * <p>
 * Acts as a facade between an application and multiple {@link DiscoveryStrategy}s.
 * Any observer subscribed to an instance of this type is notified of events such as
 * when an appliance is found or updated, or whenever an error occurs while performing discovery.
 * <p>
 * The application should subscribe to notifications using the {@link ApplianceListener} interface.
 * It's also possible to just obtain the set of available appliances using {@link #getAvailableAppliances()}
 *
 * @publicApi
 */
public class ApplianceManager {

    public interface ApplianceListener<A extends Appliance> {
        void onApplianceFound(@NonNull A foundAppliance);

        void onApplianceUpdated(@NonNull A updatedAppliance);

        void onApplianceLost(@NonNull A lostAppliance);
    }

    private final ApplianceFactory applianceFactory;

    private final Set<ApplianceListener<Appliance>> applianceListeners = new CopyOnWriteArraySet<>();
    private Map<String, Appliance> availableAppliances = new ConcurrentHashMap<>();
    private Map<String, Appliance> allAppliances = new ConcurrentHashMap<>();

    private final Handler handler = createHandler();

    private final AvailabilityListener<Appliance> applianceAvailabilityListener = new AvailabilityListener<Appliance>() {
        @Override
        public void onAvailabilityChanged(@NonNull Appliance appliance) {
            final String cppId = appliance.getNetworkNode().getCppId();
            if (appliance.isAvailable()) {
                if (!availableAppliances.containsKey(cppId)) {
                    availableAppliances.put(cppId, appliance);
                    notifyApplianceFound(appliance);
                }
            } else {
                final Appliance lostAppliance = availableAppliances.remove(cppId);

                if (lostAppliance != null) {
                    notifyApplianceLost(lostAppliance);
                }
            }
        }
    };

    private final DiscoveryStrategy.DiscoveryListener discoveryListener = new DiscoveryStrategy.DiscoveryListener() {
        @Override
        public void onDiscoveryStarted() {
        }

        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            final String cppId = networkNode.getCppId();
            if (availableAppliances.containsKey(cppId)) {
                onNetworkNodeUpdated(networkNode);
            } else if (allAppliances.containsKey(cppId)) {
                Appliance foundAppliance = allAppliances.get(cppId);
                availableAppliances.put(cppId, foundAppliance);
                notifyApplianceFound(foundAppliance);
            } else if (applianceFactory.canCreateApplianceForNode(networkNode)) {
                final Appliance appliance = (Appliance) applianceFactory.createApplianceForNode(networkNode);
                appliance.addAvailabilityListener(applianceAvailabilityListener);
                allAppliances.put(cppId, appliance);
                availableAppliances.put(cppId, appliance);
                notifyApplianceFound(appliance);
            }
        }

        @Override
        public void onNetworkNodeLost(NetworkNode networkNode) {
            final Appliance appliance = availableAppliances.get(networkNode.getCppId());

            if (appliance != null && !appliance.isAvailable()) {
                notifyApplianceLost(availableAppliances.remove(networkNode.getCppId()));
            }
        }

        @Override
        public void onNetworkNodeUpdated(NetworkNode networkNode) {
            final Appliance appliance = availableAppliances.get(networkNode.getCppId());

            if (appliance != null) {
                appliance.getNetworkNode().updateWithValuesFrom(networkNode);
                notifyApplianceUpdated(appliance);
            }
        }

        @Override
        public void onDiscoveryStopped() {
        }
    };

    /**
     * Instantiates a new ApplianceManager.
     *
     * @param discoveryStrategies the discovery strategies
     * @param applianceFactory    the appliance factory
     */
    public ApplianceManager(@NonNull Set<DiscoveryStrategy> discoveryStrategies, @NonNull ApplianceFactory applianceFactory) {
        if (discoveryStrategies.isEmpty()) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one discovery strategy.");
        }
        for (DiscoveryStrategy strategy : discoveryStrategies) {
            strategy.addDiscoveryListener(discoveryListener);
        }
        this.applianceFactory = applianceFactory;

        loadAppliancesFromPersistentStorage();
    }

    /**
     * Gets available appliances.
     *
     * @return The currently available appliances
     */
    public Set<Appliance> getAvailableAppliances() {
        return new CopyOnWriteArraySet<>(availableAppliances.values());
    }

    /**
     * Find appliance by cpp id.
     *
     * @param cppId the cpp id
     * @return the appliance
     */
    public Appliance findApplianceByCppId(final String cppId) {
        return availableAppliances.get(cppId);
    }

    /**
     * Store an appliance.
     *
     * @param appliance the appliance
     */
    public void storeAppliance(@NonNull Appliance appliance) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Add an appliance listener.
     *
     * @param applianceListener the listener
     * @return true, if the listener didn't exist yet and was therefore added
     */
    public boolean addApplianceListener(@NonNull ApplianceListener<Appliance> applianceListener) {
        return applianceListeners.add(applianceListener);
    }

    /**
     * Remove an appliance listener.
     *
     * @param applianceListener the listener
     * @return true, if the listener was present and therefore removed
     */
    public boolean removeApplianceListener(@NonNull ApplianceListener<Appliance> applianceListener) {
        return applianceListeners.remove(applianceListener);
    }

    private void loadAppliancesFromPersistentStorage() {
        // TODO
    }

    private <A extends Appliance> void notifyApplianceFound(final @NonNull A appliance) {
        DICommLog.v(DICommLog.DISCOVERY, "Appliance found " + appliance.toString());
        for (final ApplianceListener<Appliance> listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceFound(appliance);
                }
            });
        }
    }

    private void notifyApplianceUpdated(final @NonNull Appliance appliance) {
        for (final ApplianceListener<Appliance> listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceUpdated(appliance);
                }
            });
        }
    }

    private void notifyApplianceLost(final @NonNull Appliance appliance) {
        DICommLog.v(DICommLog.DISCOVERY, "Appliance lost " + appliance.toString());
        for (final ApplianceListener<Appliance> listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceLost(appliance);
                }
            });
        }
    }
}

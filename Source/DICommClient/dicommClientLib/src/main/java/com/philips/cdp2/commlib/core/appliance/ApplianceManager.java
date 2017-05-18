/*
 * Copyright (c) 2016, 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type ApplianceManager.
 * <p>
 * Acts as a facade between an application and multiple {@link DiscoveryStrategy}s.
 * Any observer subscribed to an instance of this type is notified of events such as
 * when an appliance is found or updated, or whenever an error occurs while performing discovery.
 * <p>
 * The application should subscribe to notifications using the {@link ApplianceListener} interface.
 * It's also possible to just obtain the set of available appliances using {@link #getAvailableAppliances()}
 */
public class ApplianceManager {

    public interface ApplianceListener<A extends Appliance> {
        void onApplianceFound(@NonNull A foundAppliance);

        void onApplianceUpdated(@NonNull A updatedAppliance);

        void onApplianceLost(@NonNull A lostAppliance);
    }

    private final DICommApplianceFactory applianceFactory;

    private final Set<ApplianceListener> applianceListeners = new CopyOnWriteArraySet<>();
    private Set<Appliance> availableAppliances = new CopyOnWriteArraySet<>();

    private final Handler handler = HandlerProvider.createHandler();

    private final DiscoveryStrategy.DiscoveryListener discoveryListener = new DiscoveryStrategy.DiscoveryListener() {
        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            final Appliance appliance = createAppliance(networkNode);
            if (appliance == null) {
                return;
            }

            if (availableAppliances.add(appliance)) {
                notifyApplianceFound(appliance);
            }
        }

        @Override
        public void onNetworkNodeLost(NetworkNode networkNode) {
            final Appliance appliance = createAppliance(networkNode);

            if (availableAppliances.remove(appliance)) {
                notifyApplianceLost(appliance);
            }
        }

        @Override
        public void onNetworkNodeUpdated(NetworkNode networkNode) {
            // TODO find Appliance and update in availableAppliances, notify

            for (Appliance appliance : availableAppliances) {
                if (networkNode.getCppId().equals(appliance.getNetworkNode().getCppId())) {
                    // TODO Perform merge/update
                    break;
                }
            }
        }
    };

    /**
     * Instantiates a new ApplianceManager.
     *
     * @param discoveryStrategies the discovery strategies
     * @param applianceFactory    the appliance factory
     */
    public ApplianceManager(@NonNull Set<DiscoveryStrategy> discoveryStrategies, @NonNull DICommApplianceFactory applianceFactory) {
        if (discoveryStrategies.isEmpty()) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one discovery strategy.");
        }
        for (DiscoveryStrategy strategy : discoveryStrategies) {
            strategy.addDiscoveryListener(discoveryListener);
        }

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
    public Set<Appliance> getAvailableAppliances() {
        return availableAppliances;
    }

    /**
     * Find appliance by cpp id.
     *
     * @param cppId the cpp id
     * @return the appliance
     */
    public Appliance findApplianceByCppId(final String cppId) {
        for (Appliance appliance : availableAppliances) {
            if (appliance.getNetworkNode().getCppId().equals(cppId)) {
                return appliance;
            }
        }
        return null;
    }

    /**
     * Store an appliance.
     *
     * @param <A>       the appliance type parameter
     * @param appliance the appliance
     */
    public <A extends Appliance> void storeAppliance(@NonNull A appliance) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Add an appliance listener.
     *
     * @param applianceListener the listener
     * @return true, if the listener didn't exist yet and was therefore added
     */
    public <A extends Appliance> boolean addApplianceListener(@NonNull ApplianceListener<A> applianceListener) {
        return applianceListeners.add(applianceListener);
    }

    /**
     * Remove an appliance listener.
     *
     * @param applianceListener the listener
     * @return true, if the listener was present and therefore removed
     */
    public <A extends Appliance> boolean removeApplianceListener(@NonNull ApplianceListener<A> applianceListener) {
        return applianceListeners.remove(applianceListener);
    }

    private void loadAppliancesFromPersistentStorage() {
        // TODO
    }

    private Appliance createAppliance(NetworkNode networkNode) {
        if (this.applianceFactory.canCreateApplianceForNode(networkNode)) {
            return (Appliance) applianceFactory.createApplianceForNode(networkNode);
        }
        return null;
    }

    private <A extends Appliance> void notifyApplianceFound(final @NonNull A appliance) {
        for (final ApplianceListener listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceFound(appliance);
                }
            });
        }
    }

    private <A extends Appliance> void notifyApplianceUpdated(final @NonNull A appliance) {
        for (final ApplianceListener listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceUpdated(appliance);
                }
            });
        }
    }

    private <A extends Appliance> void notifyApplianceLost(final @NonNull A appliance) {
        for (final ApplianceListener listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceLost(appliance);
                }
            });
        }
    }
}

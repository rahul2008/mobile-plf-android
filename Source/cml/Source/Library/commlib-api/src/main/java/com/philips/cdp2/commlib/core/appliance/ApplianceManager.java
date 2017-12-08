/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.store.ApplianceDatabase;
import com.philips.cdp2.commlib.core.store.NetworkNodeDatabase;
import com.philips.cdp2.commlib.core.store.NullApplianceDatabase;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.createHandler;

/**
 * The ApplianceManager acts as a facade between an application and multiple {@link DiscoveryStrategy}s.
 * <p>
 * Any observer subscribed to an instance of this type is notified of events such as
 * when an appliance is found or updated, or whenever an error occurs while performing discovery.
 * <p>
 * The application should subscribe to notifications using the {@link ApplianceListener} interface.
 * It's also possible to just obtain the set of available appliances using {@link #getAvailableAppliances()}
 *
 * @publicApi
 */
public class ApplianceManager {

    /**
     * Listen to {@link Appliance}s being found or lost.
     */
    public interface ApplianceListener {
        /**
         * Called when an {@link Appliance} is found.
         *
         * @param foundAppliance
         */
        void onApplianceFound(@NonNull Appliance foundAppliance);

        /**
         * Called when an {@link Appliance} is updated due to new information from Discovery.
         *
         * @param updatedAppliance
         */
        void onApplianceUpdated(@NonNull Appliance updatedAppliance);

        /**
         * Called when an {@link Appliance} is lost.
         *
         * @param lostAppliance
         */
        void onApplianceLost(@NonNull Appliance lostAppliance);
    }

    private final ApplianceFactory applianceFactory;

    private final Set<ApplianceListener> applianceListeners = new CopyOnWriteArraySet<>();
    private Map<String, Appliance> availableAppliances = new ConcurrentHashMap<>();
    private Map<String, Appliance> allAppliances = new ConcurrentHashMap<>();

    @NonNull
    private final Handler handler = createHandler();
    @NonNull
    private final NetworkNodeDatabase networkNodeDatabase;
    @NonNull
    private final ApplianceDatabase applianceDatabase;

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
            processDiscoveredOrLoadedNetworkNode(networkNode);
        }

        @Override
        public void onNetworkNodeLost(NetworkNode networkNode) {
            final Appliance appliance = availableAppliances.get(networkNode.getCppId());

            if (appliance != null && !appliance.isAvailable()) {
                notifyApplianceLost(availableAppliances.remove(networkNode.getCppId()));
            }
        }

        @Override
        public void onDiscoveryStopped() {
        }
    };

    private void updateAppliance(NetworkNode networkNode) {
        final Appliance appliance = availableAppliances.get(networkNode.getCppId());

        if (appliance != null) {
            appliance.getNetworkNode().updateWithValuesFrom(networkNode);
            notifyApplianceUpdated(appliance);
        }
    }

    /**
     * Instantiates a new ApplianceManager.
     *
     * @param discoveryStrategies the discovery strategies
     * @param applianceFactory    the appliance factory
     */
    public ApplianceManager(@NonNull Set<DiscoveryStrategy> discoveryStrategies,
                            @NonNull ApplianceFactory applianceFactory,
                            @NonNull NetworkNodeDatabase networkNodeDatabase,
                            @Nullable ApplianceDatabase applianceDatabase) {
        this.networkNodeDatabase = networkNodeDatabase;

        if (applianceDatabase == null) {
            this.applianceDatabase = new NullApplianceDatabase();
        } else {
            this.applianceDatabase = applianceDatabase;
        }

        if (discoveryStrategies.isEmpty()) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one discovery strategy.");
        }
        for (DiscoveryStrategy strategy : discoveryStrategies) {
            strategy.addDiscoveryListener(discoveryListener);
        }
        this.applianceFactory = applianceFactory;

        loadAllAddedAppliancesFromDatabase();
    }

    @Nullable
    private Appliance processDiscoveredOrLoadedNetworkNode(@NonNull NetworkNode networkNode) {
        final String cppId = networkNode.getCppId();
        if (availableAppliances.containsKey(cppId)) {
            updateAppliance(networkNode);
            return availableAppliances.get(cppId);
        } else if (allAppliances.containsKey(cppId)) {
            final Appliance appliance = allAppliances.get(cppId);
            availableAppliances.put(cppId, appliance);
            notifyApplianceFound(appliance);
            return appliance;
        } else if (applianceFactory.canCreateApplianceForNode(networkNode)) {
            final Appliance appliance = applianceFactory.createApplianceForNode(networkNode);
            appliance.addAvailabilityListener(applianceAvailabilityListener);
            allAppliances.put(cppId, appliance);
            availableAppliances.put(cppId, appliance);
            notifyApplianceFound(appliance);
            return appliance;
        }
        return null;
    }

    /**
     * Gets all available {@link Appliance}s.
     *
     * @return The currently available appliances
     */
    @NonNull
    public Set<Appliance> getAvailableAppliances() {
        return new CopyOnWriteArraySet<>(availableAppliances.values());
    }

    /**
     * Gets all {@link Appliance}s that this manager has found, even if no longer available.
     *
     * @return All known appliances.
     */
    @NonNull
    public Set<Appliance> getAllAppliances() {
        return new CopyOnWriteArraySet<>(allAppliances.values());
    }

    /**
     * Find appliance by cpp id.
     *
     * @param cppId the cpp id.
     * @return the appliance or <code>null</code> if it can't be found.
     */
    @Nullable
    public Appliance findApplianceByCppId(final String cppId) {
        return allAppliances.get(cppId);
    }

    /**
     * Store an {@link Appliance}.
     *
     * @param appliance the appliance
     * @return <code>true</code> if the {@link Appliance} was stored.
     */
    public boolean storeAppliance(@NonNull final Appliance appliance) {
        long rowId = networkNodeDatabase.save(appliance.getNetworkNode());
        applianceDatabase.save(appliance);

        return rowId != -1L;
    }

    /**
     * No longer persist a previously stored {@link Appliance}.
     * <p>
     * After calling this, the {@link Appliance} will no longer be stored. If the {@link Appliance}
     * is available you will still be able to communicate with it.
     *
     * @param appliance {@link Appliance} to forget.
     * @return <code>true</code> if an {@link Appliance} was removed from storage.
     */
    public boolean forgetStoredAppliance(@NonNull final Appliance appliance) {
        int rowsDeleted = networkNodeDatabase.delete(appliance.getNetworkNode());
        if (rowsDeleted > 0) {
            applianceDatabase.delete(appliance);
            applianceAvailabilityListener.onAvailabilityChanged(appliance);
        }

        return rowsDeleted > 0;
    }

    /**
     * Add an appliance listener.
     *
     * @param applianceListener the listener.
     * @return <code>true</code> if the listener didn't exist yet and was therefore added.
     * @see ApplianceListener
     */
    public boolean addApplianceListener(@NonNull ApplianceListener applianceListener) {
        return applianceListeners.add(applianceListener);
    }

    /**
     * Remove an appliance listener.
     *
     * @param applianceListener the listener.
     * @return <code>true</code> if the listener was present and therefore removed.
     * @see ApplianceListener
     */
    public boolean removeApplianceListener(@NonNull ApplianceListener applianceListener) {
        return applianceListeners.remove(applianceListener);
    }

    private void loadAllAddedAppliancesFromDatabase() {
        List<NetworkNode> networkNodes = networkNodeDatabase.getAll();

        for (final NetworkNode networkNode : networkNodes) {
            final Appliance appliance = processDiscoveredOrLoadedNetworkNode(networkNode);
            if (appliance == null) continue;

            applianceDatabase.loadDataForAppliance(appliance);
            networkNode.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    DICommLog.d(DICommLog.APPLIANCE_MANAGER, "Storing NetworkNode (because of property change)");
                    networkNodeDatabase.save(networkNode);
                }
            });
        }
    }

    private <A extends Appliance> void notifyApplianceFound(final @NonNull A appliance) {
        DICommLog.v(DICommLog.APPLIANCE_MANAGER, "Appliance found " + appliance.toString());
        for (final ApplianceListener listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceFound(appliance);
                }
            });
        }
    }

    private void notifyApplianceUpdated(final @NonNull Appliance appliance) {
        for (final ApplianceListener listener : applianceListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onApplianceUpdated(appliance);
                }
            });
        }
    }

    private void notifyApplianceLost(final @NonNull Appliance appliance) {
        DICommLog.v(DICommLog.APPLIANCE_MANAGER, "Appliance lost " + appliance.toString());
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

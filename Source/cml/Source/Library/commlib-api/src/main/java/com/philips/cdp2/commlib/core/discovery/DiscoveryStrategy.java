/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.discovery;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

import java.util.Set;

/**
 * A strategy for discovering (usually nearby) {@link Appliance}s.
 * <p>
 * The {@link DiscoveryStrategy} will return {@link NetworkNode}s through the {@link DiscoveryListener}.
 *
 * @publicApi
 */
public interface DiscoveryStrategy {

    /**
     * Callback for discovery events.
     */
    interface DiscoveryListener {

        /**
         * Called when discovery is started.
         */
        void onDiscoveryStarted();

        /**
         * Called when a {@link NetworkNode} is discovered.
         *
         * @param networkNode discovered {@link NetworkNode}
         */
        void onNetworkNodeDiscovered(NetworkNode networkNode);

        /**
         * Called when a {@link NetworkNode} is lost.
         *
         * @param networkNode lost {@link NetworkNode}
         */
        void onNetworkNodeLost(NetworkNode networkNode);

        /**
         * Called when discovery is stopped.
         */
        void onDiscoveryStopped();

    }

    /**
     * Add a {@link DiscoveryListener}.
     *
     * @param discoveryListener to add.
     */
    void addDiscoveryListener(@NonNull DiscoveryListener discoveryListener);

    /**
     * Remove a {@link DiscoveryListener}.
     *
     * @param discoveryListener to remove
     */
    void removeDiscoveryListener(@NonNull DiscoveryListener discoveryListener);

    /**
     * Start discovery using this {@link DiscoveryStrategy}.
     * <p>
     * Usually this is called by {@link CommCentral}.
     * To start all {@link DiscoveryStrategy}s one typically calls {@link CommCentral#startDiscovery()}.
     *
     * @throws MissingPermissionException    thrown if additional permissions are required.
     * @throws TransportUnavailableException thrown if underlying transports are not available.
     */
    void start() throws MissingPermissionException, TransportUnavailableException;

    /**
     * Start discovery and filter for specific device types.
     * <p>
     * Only {@link Appliance}s matching the device types provided should be returned.
     *
     * @param deviceTypes set of device types which should be filtered for.
     * @throws MissingPermissionException    thrown if additional permissions are required.
     * @throws TransportUnavailableException thrown if underlying transports are not available.
     */
    void start(@NonNull Set<String> deviceTypes) throws MissingPermissionException, TransportUnavailableException;

    /**
     * Start discovery and filter for specific device types and model ids.
     * <p>
     * Only {@link Appliance}s matching both one of the device types and one of the model ids provided should be returned.
     * Usually this is called by {@link CommCentral}.
     * To start all {@link DiscoveryStrategy}s one typically calls {@link CommCentral#startDiscovery(Set)}.
     *
     * @param deviceTypes set of device types which should be filtered for.
     * @param modelIds    set of model ids which should be filtered for.
     * @throws MissingPermissionException    thrown if additional permissions are required.
     * @throws TransportUnavailableException thrown if underlying transports are not available.
     */
    void start(@NonNull Set<String> deviceTypes, @NonNull Set<String> modelIds) throws MissingPermissionException, TransportUnavailableException;

    /**
     * Stop discovery using this {@link DiscoveryStrategy}
     * <p>
     * Usually this is called by {@link CommCentral}.
     * To stop all {@link DiscoveryStrategy}s one typically calls {@link CommCentral#stopDiscovery()}.
     */
    void stop();

    /**
     * Clear all discovered {@link NetworkNode}s.
     * <p>
     * This doesn't start or stop discovery and may be invoked at any time.
     */
    void clearDiscoveredNetworkNodes();
}

/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.communication.BleCommunicationStrategy;
import com.philips.cdp2.commlib.discovery.BleDiscoveryStrategy;
import com.philips.cdp2.commlib.exception.TransportUnavailableException;
import com.philips.cdp2.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

public final class BleContext implements TransportContext {
    private static final long BLE_DISCOVERY_TIMEOUT_MS = 20000L;

    private final BleDeviceCache deviceCache;
    private final SHNCentral shnCentral;
    private final DiscoveryStrategy discoveryStrategy;

    public BleContext(@NonNull final Context context, boolean showPopupIfBLEIsTurnedOff) throws TransportUnavailableException {
        this.deviceCache = new BleDeviceCache();
        try {
            this.shnCentral = createBlueLib(context, showPopupIfBLEIsTurnedOff);
        } catch (SHNBluetoothHardwareUnavailableException e) {
            throw new TransportUnavailableException(e);
        }
        this.shnCentral.registerDeviceDefinition(new ReferenceNodeDeviceDefinitionInfo());
        this.discoveryStrategy = new BleDiscoveryStrategy(context, deviceCache, shnCentral.getShnDeviceScanner(), BLE_DISCOVERY_TIMEOUT_MS);
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return this.discoveryStrategy;
    }

    @Override
    public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new BleCommunicationStrategy(networkNode.getCppId(), this.deviceCache);
    }

    /**
     * Gets the SHNCentral instance.
     *
     * @return the SHNCentral instance
     * @deprecated only here for backwards compatibility
     */
    @Deprecated
    public SHNCentral getShnCentral() {
        return shnCentral;
    }

    private SHNCentral createBlueLib(Context context, boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
        SHNCentral.Builder builder = new SHNCentral.Builder(context);
        builder.showPopupIfBLEIsTurnedOff(showPopupIfBLEIsTurnedOff);

        return builder.create();
    }
}

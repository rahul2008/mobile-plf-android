/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.bluelib.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.communication.BleCommunicationStrategy;
import com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.concurrent.Executors;

public class BleTransportContext implements TransportContext {

    private final BleDeviceCache deviceCache;
    private final SHNCentral shnCentral;
    private final DiscoveryStrategy discoveryStrategy;

    /**
     * Instantiates a new BleTransportContext.
     * <p>
     * This constructor implicitly disables the showing of a popup when BLE is turned off.
     * </p>
     *
     * @param context the context
     */
    public BleTransportContext(@NonNull final Context context)  {
        this(context, false);
    }

    /**
     * Instantiates a new BleTransportContext.
     *
     * @param context                   the context
     * @param showPopupIfBLEIsTurnedOff the show popup if BLE is turned off
     * @throws TransportUnavailableException the transport unavailable exception
     */
    public BleTransportContext(@NonNull final Context context, boolean showPopupIfBLEIsTurnedOff) {
        this.deviceCache = new BleDeviceCache(Executors.newSingleThreadScheduledExecutor());
        try {
            SHNLogger.registerLogger(new SHNLogger.LogCatLogger());
            this.shnCentral = createBlueLib(context, showPopupIfBLEIsTurnedOff);
        } catch (SHNBluetoothHardwareUnavailableException e) {
            throw new TransportUnavailableException("Bluetooth hardware unavailable.", e);
        }
        this.shnCentral.registerDeviceDefinition(new ReferenceNodeDeviceDefinitionInfo());
        this.discoveryStrategy = new BleDiscoveryStrategy(context, deviceCache, shnCentral.getShnDeviceScanner(), shnCentral);
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return this.discoveryStrategy;
    }

    @Override
    public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new BleCommunicationStrategy(networkNode.getCppId(), this.deviceCache);
    }

    private SHNCentral createBlueLib(Context context, boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
        SHNCentral.Builder builder = new SHNCentral.Builder(context);
        builder.showPopupIfBLEIsTurnedOff(showPopupIfBLEIsTurnedOff);

        return builder.create();
    }
}

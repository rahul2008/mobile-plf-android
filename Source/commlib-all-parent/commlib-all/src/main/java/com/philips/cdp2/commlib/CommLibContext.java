/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.pins.shinelib.SHNCentral;

/**
 * The type CommLibContext.
 *
 * @param <A> the type parameter indicating the {@link DICommAppliance}
 */
public final class CommLibContext<A extends DICommAppliance> {

    @NonNull
    private final DiscoveryManager<A> mDiscoveryManager;

    @NonNull
    private final BleDeviceCache mBleDeviceCache;

    @NonNull
    private final SHNCentral mShnCentral;

    /**
     * Instantiates a new CommLib context.
     *
     * @param discoveryManager the discovery manager
     * @param shnCentral       the shn central
     * @param deviceCache      the device cache
     */
    CommLibContext(final @NonNull DiscoveryManager<A> discoveryManager, final @NonNull SHNCentral shnCentral, @NonNull BleDeviceCache deviceCache) {
        mDiscoveryManager = discoveryManager;
        mShnCentral = shnCentral;
        mBleDeviceCache = deviceCache;
    }

    /**
     * Gets DiscoveryManager.
     *
     * @return the DiscoveryManager for this {@link CommLibContext}
     */
    @NonNull
    public DiscoveryManager<A> getDiscoveryManager() {
        return mDiscoveryManager;
    }

    /**
     * Gets SHNCentral.
     *
     * @return the SHNCentral for this {@link CommLibContext}
     */
    @NonNull
    public SHNCentral getShnCentral() {
        return mShnCentral;
    }

    /**
     * Gets BleDeviceCache.
     *
     * @return the BleDeviceCache for this {@link CommLibContext}
     */
    @NonNull
    public BleDeviceCache getBleDeviceCache() {
        return mBleDeviceCache;
    }
}

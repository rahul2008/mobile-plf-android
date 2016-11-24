/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp2.commlib.discovery.BleDiscoveryStrategy;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

/**
 * The type CommLibBuilder.
 *
 * @param <A> the appliance type
 */
public final class CommLibContextBuilder<A extends DICommAppliance> {

    public interface ApplianceFactoryBuilder<B extends DICommAppliance> {
        DICommApplianceFactory<B> create(@NonNull BleDeviceCache bleDeviceCache);
    }

    private final Context mContext;
    private final ApplianceFactoryBuilder<A> applianceFactoryBuilder;
    private DICommApplianceDatabase<A> mApplianceDatabase;
    private CloudController mCloudController;
    private DICommApplianceFactory<A> mApplianceFactory;

    /**
     * Instantiates a new CommLibBuilder.
     *
     * @param context the context
     */
    public CommLibContextBuilder(@NonNull final Context context, ApplianceFactoryBuilder<A> applianceFactoryBuilder) {
        mContext = context;
        this.applianceFactoryBuilder = applianceFactoryBuilder;
    }

    /**
     * Create CommLibContext.
     *
     * @return the CommLibContext
     */
    public final CommLibContext create() {
        BleDeviceCache deviceCache = new BleDeviceCache();

        SHNCentral shnCentral;
        try {
            shnCentral = createBlueLib();
        } catch (SHNBluetoothHardwareUnavailableException e) {
            throw new CommLibInitializationException(e);
        }

        DICommApplianceFactory<A> applianceFactory = applianceFactoryBuilder.create(deviceCache);

        DiscoveryManager<A> discoveryManager = createCommLib(applianceFactory);

        // TODO Use defaults?
        DiscoveryStrategy discoveryStrategy = new BleDiscoveryStrategy(deviceCache, shnCentral.getShnDeviceScanner(), 10000L);

        return new CommLibContext<A>(discoveryStrategy, discoveryManager, shnCentral, deviceCache);
    }

    /**
     * Create a DiscoveryManager.
     *
     * @param applianceFactory the appliance factory
     * @return the DiscoveryManager
     */
    private DiscoveryManager<A> createCommLib(DICommApplianceFactory<A> applianceFactory) {
        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(mContext, applianceFactory, mApplianceDatabase, mCloudController);
        }
        return (DiscoveryManager<A>) DiscoveryManager.getInstance();
    }

    private SHNCentral createBlueLib() throws SHNBluetoothHardwareUnavailableException {
        SHNCentral.Builder builder = new SHNCentral.Builder(mContext);
        builder.showPopupIfBLEIsTurnedOff(true);

        return builder.create();
    }

    /**
     * Sets appliance database.
     *
     * @param database the database
     * @return the appliance database
     */
    public CommLibContextBuilder<A> setApplianceDatabase(@NonNull DICommApplianceDatabase<A> database) {
        mApplianceDatabase = database;
        return this;
    }

    /**
     * Sets cloud controller.
     *
     * @param cloudController the cloud controller
     * @return the cloud controller
     */
    public CommLibContextBuilder<A> setCloudController(@NonNull CloudController cloudController) {
        mCloudController = cloudController;
        return this;
    }
}

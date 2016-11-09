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
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

/**
 * The type CommLibBuilder.
 *
 * @param <A> the appliance type
 */
public final class CommLibContextBuilder<A extends DICommAppliance> {
    private final Context mContext;
    private DICommApplianceDatabase<A> mApplianceDatabase;
    private CloudController mCloudController;
    private DICommApplianceFactory<A> mApplianceFactory;
    private SHNCentral mShnCentral;

    /**
     * Instantiates a new CommLibBuilder.
     *
     * @param context the context
     */
    public CommLibContextBuilder(@NonNull final Context context) {
        mContext = context;
    }

    /**
     * Create CommLibContext.
     *
     * @return the CommLibContext
     */
    public final CommLibContext create() {
        // TODO register DiComm plugin
        // TODO initialize BLEStrategy

        try {
            initBlueLib();
        } catch (SHNBluetoothHardwareUnavailableException e) {
            throw new CommLibInitializationException(e);
        }
        createDiscoveryManager();

        DiscoveryManager<A> discoveryManager = (DiscoveryManager<A>) DiscoveryManager.getInstance();

        return new CommLibContext<A>(discoveryManager, mShnCentral);
    }

    /**
     * Create a DiscoveryManager.
     *
     * @return the DiscoveryManager
     */
    private void createDiscoveryManager() {
        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(mContext, mApplianceFactory, mApplianceDatabase, mCloudController);
        }
    }

    private void initBlueLib() throws SHNBluetoothHardwareUnavailableException {
        SHNCentral.Builder builder = new SHNCentral.Builder(mContext);
        builder.showPopupIfBLEIsTurnedOff(true);

        mShnCentral = builder.create();

        // FIXME Use DiComm plugin
        // SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = new DeviceDefinitionInfoReferenceBoard();
        // mShnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);
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
     * Sets appliance factory.
     *
     * @param applianceFactory the appliance factory
     * @return the appliance factory
     */
    public CommLibContextBuilder<A> setApplianceFactory(DICommApplianceFactory<A> applianceFactory) {
        mApplianceFactory = applianceFactory;
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

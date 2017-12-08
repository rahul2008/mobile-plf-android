/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;

/**
 * You can provide an {@link ApplianceDatabase} to {@link CommCentral} to store
 * additional data on an {@link Appliance} whenever an {@link Appliance} gets persisted.
 * <p>
 * {@link Appliance} storage will work even when you do not provide an {@link ApplianceDatabase} as
 * Commlib will store all information in the {@link NetworkNode} itself. Trying to persist data in
 * the {@link NetworkNode} with your own {@link ApplianceDatabase} is not advisable.
 *
 * @publicApi
 */
public interface ApplianceDatabase {

    /**
     * Save all additional data from an {@link Appliance}.
     *
     * @param appliance {@link Appliance} that is being stored by CommLib.
     * @return <code>true</code> if the data was persisted.
     */
    boolean save(@NonNull Appliance appliance);

    /**
     * Load all additional data into an {@link Appliance}.
     *
     * @param appliance {@link Appliance} that is being loaded by CommLib.
     */
    void loadDataForAppliance(@NonNull Appliance appliance);

    /**
     * Remove all additional data from an {@link Appliance}.
     *
     * @param appliance {@link Appliance} that is no longer persisted by CommLib.
     * @return <code>true</code> if successfully deleted.
     */
    boolean delete(@NonNull Appliance appliance);

}

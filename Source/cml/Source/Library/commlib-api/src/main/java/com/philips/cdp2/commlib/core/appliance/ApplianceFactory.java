/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Set;

/**
 * Factory for <code>Appliances</code>. This interface is implemented by an app developer to create
 * <code>Appliances</code> for specific hardware.
 *
 * @publicApi
 */
public interface ApplianceFactory {

    /**
     * Called when Discovery finds any DiComm appliance on the network.
     *
     * @param networkNode The network node for which the factory has to check if it can create a
     *                    {link Appliance} (e.g. by checking the model name and/or type).
     * @return true if it can create, false if not
     */
    boolean canCreateApplianceForNode(@NonNull NetworkNode networkNode);

    /**
     * Create an {@link Appliance} for the supplied {@link NetworkNode}.
     *
     * @param networkNode <code>NetworkNode</code> to create an <code>Appliance</code> for.
     * @return Created <code>Appliance</code>
     */
    Appliance createApplianceForNode(@NonNull NetworkNode networkNode);

    /**
     * Indicate the device types supported by this <code>ApplianceFactory</code>.
     * <p>
     * This can be used to filter during discovery but will not guarantee that only
     * {@link NetworkNode}s for these device types are discovered.
     *
     * @return Set of supported device types.
     */
    Set<String> getSupportedDeviceTypes();
}

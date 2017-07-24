/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Set;

public interface DICommApplianceFactory<T> {

    /**
     * Called when Discovery finds any DiComm appliance on the network.
     *
     * @param networkNode The network node for which the factory has to check if it can create a {link Appliance} (e.g. by checking the model name and/or type).
     * @return true if it can create, false if not
     */
    boolean canCreateApplianceForNode(NetworkNode networkNode);

    T createApplianceForNode(NetworkNode networkNode);

    Set<String> getSupportedDeviceTypes();
}

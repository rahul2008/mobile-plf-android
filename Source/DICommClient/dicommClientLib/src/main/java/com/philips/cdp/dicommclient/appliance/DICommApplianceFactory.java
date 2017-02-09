/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Set;

public interface DICommApplianceFactory<T> {

    /**
     * Called when {@link DiscoveryManager} finds any DiComm appliance on the network.
     *
     * @param networkNode The network node for which the factory has to check if it can wait a {link Appliance} (e.g. by checking the model name and/or type).
     * @return true if it can wait, false if not
     */
    boolean canCreateApplianceForNode(NetworkNode networkNode);

    T createApplianceForNode(NetworkNode networkNode);

    Set<String> getSupportedModelNames();
}

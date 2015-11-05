/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;

public abstract class DICommApplianceFactory<T> {

    /**
     * Called when {@link DiscoveryManager} finds any DiComm appliance on the network.
     *
     * @param networkNode The network node for which the factory has to check if it can create a {link DICommAppliance} (e.g. by checking the model name and/or type).
     * @return true if it can create, false if not
     */
    public abstract boolean canCreateApplianceForNode(NetworkNode networkNode);

    public abstract T createApplianceForNode(NetworkNode networkNode);
}

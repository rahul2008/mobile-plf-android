/*
 * © Koninklijke Philips N.V., 2015, 2017.
 *   All rights reserved.
 */
package com.philips.cdp.dicommclient.testutil;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.NullCommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class TestAppliance extends Appliance {

    public TestAppliance(NetworkNode networkNode) {
        super(networkNode, new NullCommunicationStrategy());
    }

    @Override
    public String getDeviceType() {
        return null;
    }
}

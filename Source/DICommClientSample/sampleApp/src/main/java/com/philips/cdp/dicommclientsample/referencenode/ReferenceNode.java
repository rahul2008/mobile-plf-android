/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclientsample.referencenode;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class ReferenceNode extends Appliance {
    public static final String DEVICETYPE = "BCM943903";

    public ReferenceNode(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }
}

/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class HypnogramDataPort extends GenericPort<HypnogramDataPortProperties> {
    private long sessionNumber;

    public HypnogramDataPort(CommunicationStrategy communicationStrategy, String name, int productId, Class<HypnogramDataPortProperties> propertiesClass) {
        super(communicationStrategy, name, productId, propertiesClass);
    }

    @Override
    public String getDICommPortName() {
        return String.format(name, sessionNumber);
    }

    public void setSpecificSession(long sessionnum) {
        sessionNumber = sessionnum;
    }
}

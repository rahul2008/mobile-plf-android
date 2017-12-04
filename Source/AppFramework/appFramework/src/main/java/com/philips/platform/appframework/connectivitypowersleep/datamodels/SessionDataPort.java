/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class SessionDataPort<T extends PortProperties> extends GenericPort<T> {

    private long sessionNumber;

    public SessionDataPort(CommunicationStrategy communicationStrategy, String name, int productId, Class<T> propertiesClass) {
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

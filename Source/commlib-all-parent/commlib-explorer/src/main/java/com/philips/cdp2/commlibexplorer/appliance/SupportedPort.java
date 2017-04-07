/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

public abstract class SupportedPort<T extends PortProperties> extends DICommPort<T> {

    public SupportedPort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    public abstract String getPortName();

    public abstract String getErrorText();

    public abstract boolean isEnabled();

    public abstract String getStatusText();
}

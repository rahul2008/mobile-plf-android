/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;

public class NativePort implements SupportedPort {

    private DICommPort port;
    private String name;

    public NativePort(DICommPort port) {
        this.port = port;
        this.name = port.getClass().getSimpleName();
    }

    @Override
    public String getPortName() {
        return name;
    }

    public DICommPort getPort() {
        return port;
    }

    @Override
    public String getErrorText() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getStatusText() {
        return "";
    }
}

/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp2.commlib.core.port.PortProperties;

public class WifiUIPortProperties implements PortProperties {

    private String setup;
    private String connection;
    private String pairing;

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getPairing() {
        return pairing;
    }

    public void setPairing(String pairing) {
        this.pairing = pairing;
    }
}

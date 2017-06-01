/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp2.commlib.core.port.PortProperties;

public class SecurityPortProperties implements PortProperties {
    private String key;
    private String diffie;
    private String hellman;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDiffie() {
        return diffie;
    }

    public void setDiffie(String diffie) {
        this.diffie = diffie;
    }

    public String getHellman() {
        return hellman;
    }

    public void setHellman(String hellman) {
        this.hellman = hellman;
    }
}

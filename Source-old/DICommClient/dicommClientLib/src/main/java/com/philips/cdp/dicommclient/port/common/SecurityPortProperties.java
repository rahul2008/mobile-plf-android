/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.port.PortProperties;

public class SecurityPortProperties implements PortProperties {
    public static final String DIFFIE = "diffie";
    public static final String HELLMAN = "hellman";
    public static final String KEY = "key";

    private String key;
    private String diffie;
    private String hellman;

    @Nullable
    public String getKey() {
        return key;
    }

    @Nullable
    public String getDiffie() {
        return diffie;
    }

    public void setDiffie(String diffie) {
        this.diffie = diffie;
    }

    @Nullable
    public String getHellman() {
        return hellman;
    }
}

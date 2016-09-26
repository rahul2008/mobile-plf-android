package com.philips.platform.core.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserCredentials {
    private String hsdpUUID;
    private String hsdpToken;
    private String janrainUUID;
    private String janrainToken;

    public UserCredentials(final String hsdpUUID, final String hsdpToken, final String janrainUUID, final String janrainToken) {
        this.hsdpUUID = hsdpUUID;
        this.hsdpToken = hsdpToken;
        this.janrainUUID = janrainUUID;
        this.janrainToken = janrainToken;
    }

    public String getHsdpUUID() {
        return hsdpUUID;
    }

    public String getHsdpToken() {
        return hsdpToken;
    }

    public String getJanrainUUID() {
        return janrainUUID;
    }

    public String getJanrainToken() {
        return janrainToken;
    }
}

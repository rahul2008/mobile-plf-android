/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.pairing;

/**
 * The type PairingEntity.
 * <p>
 * Value object that stores entity reference properties used during pairing.
 */
public class PairingEntity {

    public String provider;

    public String id;

    public String type;

    public String credentials;

    public PairingEntity(final String provider, final String id, final String type, final String credentials) {
        this.provider = provider;
        this.id = id;
        this.type = type;
        this.credentials = credentials;
    }
}

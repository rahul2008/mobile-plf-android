/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

public class AppPairingHandlerRelationship extends BasePairingHandlerRelationship {

    public AppPairingHandlerRelationship(String cppId, String provider, String applianceCppId, String applicanceDeviceType) {
        super(cppId, provider, applicanceDeviceType, null, applianceCppId, applicanceDeviceType);
    }

    @Override
    public PairingEntityReference getTrustorEntity() {
        return null;
    }
}

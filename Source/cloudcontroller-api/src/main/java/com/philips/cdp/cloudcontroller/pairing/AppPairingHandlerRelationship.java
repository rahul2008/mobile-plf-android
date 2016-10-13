/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

/**
 * The class AppPairingHandlerRelationship.
 */
public class AppPairingHandlerRelationship extends BasePairingHandlerRelationship {

    /**
     * Instantiates a new AppPairingHandlerRelationship.
     *
     * @param cppId                the cpp id
     * @param provider             the provider
     * @param applianceCppId       the appliance cpp id
     * @param applicanceDeviceType the applicance device type
     */
    public AppPairingHandlerRelationship(String cppId, String provider, String applianceCppId, String applicanceDeviceType) {
        super(cppId, provider, applicanceDeviceType, null, applianceCppId, applicanceDeviceType);
    }

    @Override
    public PairingEntityReference getTrustorEntity() {
        return null;
    }
}

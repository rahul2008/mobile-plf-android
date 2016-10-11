/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

import com.philips.icpinterface.data.PairingEntitiyReference;

public class AppPairingHandlerRelationship extends PairingHandlerRelationship {

    public AppPairingHandlerRelationship(String cppId, String provider, String applianceCppId, String applicanceDeviceType) {
        super(cppId, provider, applicanceDeviceType, null, applianceCppId, applicanceDeviceType);
    }

    @Override
    public PairingEntitiyReference getTrustorEntity() {
        return null;
    }
}

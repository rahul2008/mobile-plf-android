package com.philips.cdp.cloudcontroller.pairing;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface PairingHandlerRelationship {
    String getCppId();

    String getProvider();

    String getType();

    String getCredentials();

    String getApplianceCppId();

    String getApplianceDeviceType();

    PairingEntityReference getTrustorEntity();

    PairingEntityReference getTrusteeEntity();
}

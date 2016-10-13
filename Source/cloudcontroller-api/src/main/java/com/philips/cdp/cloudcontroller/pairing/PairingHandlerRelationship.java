/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.cloudcontroller.pairing;

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

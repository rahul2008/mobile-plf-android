/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.cloudcontroller.pairing;

/**
 * The interface PairingHandlerRelationship.
 * <p>
 * Provides pairing relationship information.
 */
public interface PairingHandlerRelationship {
    /**
     * Gets cpp id.
     *
     * @return the cpp id
     */
    String getCppId();

    /**
     * Gets provider.
     *
     * @return the provider
     */
    String getProvider();

    /**
     * Gets type.
     *
     * @return the type
     */
    String getType();

    /**
     * Gets credentials.
     *
     * @return the credentials
     */
    String getCredentials();

    /**
     * Gets appliance cpp id.
     *
     * @return the appliance cpp id
     */
    String getApplianceCppId();

    /**
     * Gets appliance device type.
     *
     * @return the appliance device type
     */
    String getApplianceDeviceType();

    /**
     * Gets trustor entity.
     *
     * @return the trustor entity
     */
    PairingEntityReference getTrustorEntity();

    /**
     * Gets trustee entity.
     *
     * @return the trustee entity
     */
    PairingEntityReference getTrusteeEntity();
}

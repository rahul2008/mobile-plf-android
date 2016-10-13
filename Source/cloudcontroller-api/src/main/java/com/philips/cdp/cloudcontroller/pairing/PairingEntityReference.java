/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

/**
 * The type PairingEntityReference.
 * <p>
 * Value object that stores entity reference properties used during pairing.
 */
public class PairingEntityReference {
    /**
     * The Entity ref provider.
     */
    public String entityRefProvider;
    /**
     * The Entity ref id.
     */
    public String entityRefId;
    /**
     * The Entity ref type.
     */
    public String entityRefType;
    /**
     * The Entity ref credentials.
     */
    public String entityRefCredentials;
}

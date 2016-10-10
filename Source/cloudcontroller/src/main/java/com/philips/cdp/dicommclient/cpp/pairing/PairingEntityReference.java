/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp.pairing;

import com.philips.icpinterface.data.PairingEntitiyReference;

/**
 * Class PairingEntityReference.
 * <p>
 * Acts as a value object to pass properties of an internal {@link PairingEntitiyReference}
 * without exposing it across this component's boundary.
 * </p>
 */
public class PairingEntityReference {
    public String entityRefProvider;
    public String entityRefId;
    public String entityRefType;
    public String entityRefCredentials;

    public final PairingEntitiyReference getInternalRepresentation() {
        PairingEntitiyReference result = new PairingEntitiyReference();
        result.entityRefCredentials = this.entityRefCredentials;
        result.entityRefId = this.entityRefId;
        result.entityRefProvider = this.entityRefProvider;
        result.entityRefType = this.entityRefType;

        return result;
    }
}

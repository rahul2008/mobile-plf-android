/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * The type PairingRelation.
 */
public class PairingRelation {

    @NonNull
    private final PairingEntity trustor;

    @NonNull
    private final PairingEntity trustee;

    @NonNull
    private final String type;

    public PairingRelation(@NonNull PairingEntity trustor, @NonNull PairingEntity trustee, @NonNull String relationType) {
        this.trustor = trustor;
        this.trustee = trustee;
        this.type = relationType;
    }

    @NonNull
    public PairingEntity getTrustorEntity() {
        return trustor;
    }

    @NonNull
    public PairingEntity getTrusteeEntity() {
        return trustee;
    }

    @NonNull
    public String getType() {
        return type;
    }
}


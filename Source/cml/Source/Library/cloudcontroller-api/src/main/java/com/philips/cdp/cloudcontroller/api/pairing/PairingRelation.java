/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.pairing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * The type PairingRelation.
 */
public class PairingRelation {

    @Nullable
    private final PairingEntity trustor;

    @Nullable
    private final PairingEntity trustee;

    @NonNull
    private final String type;

    @NonNull
    private final Set<String> permissions;

    public PairingRelation(@Nullable PairingEntity trustor, @Nullable PairingEntity trustee, @NonNull String relationType) {
        this.trustor = trustor;
        this.trustee = trustee;
        this.type = relationType;
        this.permissions = new HashSet<>();
    }

    @Nullable
    public PairingEntity getTrustorEntity() {
        return trustor;
    }

    @Nullable
    public PairingEntity getTrusteeEntity() {
        return trustee;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public Set<String> getPermissions() {
        return permissions;
    }

    public void addPermission(@NonNull String permission) {
        this.permissions.add(permission);
    }
}


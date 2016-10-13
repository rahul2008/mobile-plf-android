/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

import android.util.Log;

import com.philips.cdp.cloudcontroller.util.LogConstants;

public abstract class BasePairingHandlerRelationship implements PairingHandlerRelationship {
    private final String mCppId;
    private final String mProvider;
    private final String mType;
    private final String mCredentials;
    private final String mApplianceCppId;
    private final String mApplianceDeviceType;

    BasePairingHandlerRelationship(String cppId, String provider, String type, String credentials, String applianceCppId, String applianceDeviceType) {
        this.mCppId = cppId;
        this.mProvider = provider;
        this.mType = type;
        this.mCredentials = credentials;
        this.mApplianceCppId = applianceCppId;
        this.mApplianceDeviceType = applianceDeviceType;
    }

    @Override
    public abstract PairingEntityReference getTrustorEntity();

    @Override
    public PairingEntityReference getTrusteeEntity() {
        PairingEntityReference pairingTrustee = new PairingEntityReference();
        pairingTrustee.entityRefId = mApplianceCppId;
        pairingTrustee.entityRefProvider = mProvider;
        pairingTrustee.entityRefType = mApplianceDeviceType;
        pairingTrustee.entityRefCredentials = null;

        Log.i(LogConstants.PAIRING, "Appliance entityRefId" + pairingTrustee.entityRefId);
        Log.i(LogConstants.PAIRING, "Appliance entityRefType" + pairingTrustee.entityRefType);

        return pairingTrustee;
    }

    @Override
    public String getCppId() {
        return mCppId;
    }

    @Override
    public String getProvider() {
        return mProvider;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public String getCredentials() {
        return mCredentials;
    }

    @Override
    public String getApplianceCppId() {
        return mApplianceCppId;
    }

    @Override
    public String getApplianceDeviceType() {
        return mApplianceDeviceType;
    }
}


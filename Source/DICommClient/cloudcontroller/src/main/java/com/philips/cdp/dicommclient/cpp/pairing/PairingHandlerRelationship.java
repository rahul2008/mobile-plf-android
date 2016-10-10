/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp.pairing;

import android.util.Log;

import com.philips.cdp.dicommclient.util.LogConstants;
import com.philips.icpinterface.data.PairingEntitiyReference;

public abstract class PairingHandlerRelationship {
    private final String mCppId;
    private final String mProvider;
    private final String mType;
    private final String mCredentials;
    private final String mApplianceCppId;
    private final String mApplianceDeviceType;

    PairingHandlerRelationship(String cppId, String provider, String type, String credentials, String applianceCppId, String applianceDeviceType) {
        this.mCppId = cppId;
        this.mProvider = provider;
        this.mType = type;
        this.mCredentials = credentials;
        this.mApplianceCppId = applianceCppId;
        this.mApplianceDeviceType = applianceDeviceType;
    }

    public abstract PairingEntitiyReference getTrustorEntity();

    public PairingEntitiyReference getTrusteeEntity() {
        PairingEntitiyReference pairingTrustee = new PairingEntitiyReference();
        pairingTrustee.entityRefId = mApplianceCppId;
        pairingTrustee.entityRefProvider = mProvider;
        pairingTrustee.entityRefType = mApplianceDeviceType;
        pairingTrustee.entityRefCredentials = null;

        Log.i(LogConstants.PAIRING, "Appliance entityRefId" + pairingTrustee.entityRefId);
        Log.i(LogConstants.PAIRING, "Appliance entityRefType" + pairingTrustee.entityRefType);

        return pairingTrustee;
    }

    public String getCppId() {
        return mCppId;
    }

    public String getProvider() {
        return mProvider;
    }

    public String getType() {
        return mType;
    }

    public String getCredentials() {
        return mCredentials;
    }

    public String getApplianceCppId() {
        return mApplianceCppId;
    }

    public String getApplianceDeviceType() {
        return mApplianceDeviceType;
    }
}


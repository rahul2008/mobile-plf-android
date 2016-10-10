/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp.pairing;

import android.util.Log;

import com.philips.cdp.dicommclient.util.LogConstants;
import com.philips.icpinterface.data.PairingEntitiyReference;

public class UserPairingHandlerRelationship extends PairingHandlerRelationship {

    public UserPairingHandlerRelationship(String cppId, String provider, String type, String credentials, String applianceCppId, String applicanceDeviceType) {
        super(cppId, provider, type, credentials, applianceCppId, applicanceDeviceType);
    }

    @Override
    public PairingEntitiyReference getTrustorEntity() {
        PairingEntitiyReference pairingTrustor = new PairingEntitiyReference();
        pairingTrustor.entityRefId = getCppId();
        pairingTrustor.entityRefProvider = getProvider();
        pairingTrustor.entityRefType = getType();
        pairingTrustor.entityRefCredentials = getCredentials();

        Log.i(LogConstants.PAIRING, "User entityRefId" + pairingTrustor.entityRefId);
        Log.i(LogConstants.PAIRING, "User entityRefType" + pairingTrustor.entityRefType);

        return pairingTrustor;
    }
}
